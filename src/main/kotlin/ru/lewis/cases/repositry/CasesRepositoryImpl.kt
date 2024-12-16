package ru.lewis.cases.repositry

import jakarta.inject.Inject
import jakarta.inject.Singleton
import me.lucko.helper.terminable.TerminableConsumer
import me.lucko.helper.terminable.module.TerminableModule
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import ru.lewis.cases.model.casehandling.CaseData
import ru.lewis.cases.repositry.entity.CaseHibernateEntity
import ru.lewis.cases.service.ConfigurationService
import kotlin.time.Duration.Companion.seconds

@Deprecated("The old implementation of the repository via hibernate is used here, a new one will be coming soon...")
@Singleton
class CasesRepositoryImpl @Inject constructor(
    private val plugin: Plugin,
    private val configurationService: ConfigurationService
) : CasesRepository, TerminableModule {

    private val databaseCfg get() = configurationService.config
    private lateinit var sessionFactory: SessionFactory

    override fun hasCase(case: CaseData, player: Player): Boolean {
        val session: Session = sessionFactory.openSession()
        val transaction: Transaction = session.beginTransaction()

        try {
            val playerUUID = player.uniqueId
            val caseId = case.id

            val query = session.createQuery(
                "SELECT 1 FROM CaseHibernateEntity WHERE uuid = :uuid AND caseId = :caseId"
            )
            query.setParameter("uuid", playerUUID)
            query.setParameter("caseId", caseId)

            val result = query.uniqueResult()

            transaction.commit()
            return result != null
        } catch (e: Exception) {
            transaction.rollback()
            throw e
        } finally {
            session.close()
        }
    }

    override fun setCase(case: CaseData, player: Player, count: Int) {
        val session: Session = sessionFactory.openSession()
        val transaction: Transaction = session.beginTransaction()

        try {
            val playerUUID = player.uniqueId
            val caseId = case.id

            val existingCaseQuery = session.createQuery(
                "FROM CaseHibernateEntity WHERE uuid = :uuid AND caseId = :caseId", CaseHibernateEntity::class.java
            )
            existingCaseQuery.setParameter("uuid", playerUUID)
            existingCaseQuery.setParameter("caseId", caseId)

            val caseEntity = existingCaseQuery.uniqueResult()

            if (count == 0) {
                // Если count == 0, удаляем запись из базы данных
                if (caseEntity != null) {
                    session.delete(caseEntity)
                }
            } else {
                if (caseEntity == null) {
                    // Если записи нет, создаем новую
                    val newCaseEntity = CaseHibernateEntity(playerUUID, caseId, count)
                    session.save(newCaseEntity)
                } else {
                    // Если запись найдена, обновляем count
                    caseEntity.count = count
                    session.update(caseEntity)
                }
            }

            transaction.commit()
        } catch (e: Exception) {
            transaction.rollback()
            throw e
        } finally {
            session.close()
        }
    }


    override fun getCaseCount(case: CaseData, player: Player): Int {
        val session: Session = sessionFactory.openSession()
        val transaction: Transaction = session.beginTransaction()

        try {
            val playerUUID = player.uniqueId
            val caseId = case.id

            val query = session.createQuery(
                "SELECT count FROM CaseHibernateEntity WHERE uuid = :uuid AND caseId = :caseId"
            )
            query.setParameter("uuid", playerUUID)
            query.setParameter("caseId", caseId)

            val count = query.uniqueResult() as? Int
            transaction.commit()
            return count ?: 0
        } catch (e: Exception) {
            transaction.rollback()
            throw e
        } finally {
            session.close()
        }
    }

    override fun reset(player: Player) {
        val session: Session = sessionFactory.openSession()
        val transaction: Transaction = session.beginTransaction()

        try {
            val playerUUID = player.uniqueId

            // Удаляем все записи, связанные с данным игроком
            val query = session.createQuery(
                "DELETE FROM CaseHibernateEntity WHERE uuid = :uuid"
            )
            query.setParameter("uuid", playerUUID)
            query.executeUpdate()

            transaction.commit()
        } catch (e: Exception) {
            transaction.rollback()
            throw e
        } finally {
            session.close()
        }
    }


    private fun connectMariaDB(): SessionFactory {

        return SessionFactoryBuilder.build {
            classLoader = plugin::class.java.classLoader

            val cfg = databaseCfg.database

            user = cfg.user
            password = cfg.password
            driver = org.mariadb.jdbc.Driver::class
            url = "jdbc:mariadb://${cfg.address}:${cfg.port}/${cfg.database}${parametersToString(cfg.parameters)}"

            hikariProperties["maximumPoolSize"] = Runtime.getRuntime().availableProcessors().toString()
            hikariProperties["connectionTimeout"] = 10.seconds.inWholeMilliseconds.toString()
            hikariProperties["poolName"] = plugin.name.plus("/mariadb")

            register<CaseHibernateEntity>()

        }

    }

    private fun parametersToString(parameters: List<String>): String {
        return parameters.joinToString(prefix = "?", separator = "&")
    }

    override fun setup(p0: TerminableConsumer) {
        sessionFactory = connectMariaDB()
    }


}