package ru.lewis.cases.model.box

import jakarta.inject.Inject
import jakarta.inject.Singleton
import me.lucko.helper.terminable.TerminableConsumer
import me.lucko.helper.terminable.module.TerminableModule
import org.bukkit.plugin.Plugin
import ru.lewis.cases.model.casehandling.Gift
import ru.lewis.cases.model.casehandling.CaseData
import ru.lewis.cases.repositry.CasesRepository
import ru.lewis.cases.service.ConfigurationService

@Singleton
class BoxManager @Inject constructor(
    private val plugin: Plugin,
    private val configurationService: ConfigurationService,
    private val repository: CasesRepository
) : TerminableModule {

    private val boxes: MutableList<ActiveBox> = mutableListOf()

    override fun setup(p0: TerminableConsumer) {
        reload()
    }

    fun reload() {

        boxes.clear()

        val caseList: MutableList<CaseData> = mutableListOf()
        configurationService.config.boxList.forEach {
            it.caseList.forEach { case ->
                caseList.add(
                    CaseData(
                        case.id,
                        case.gifts.map { gift ->
                            Gift(
                                gift.name,
                                gift.chance,
                                gift.item,
                                gift.title,
                                gift.items,
                                gift.commandsOnConsole
                            )
                        }.toList(),
                        case.item
                    )
                )
            }
        }

        configurationService.config.boxList.forEach {
            boxes.add(
                ActiveBox(
                    plugin,
                    it.location.toBukkitLocation(),
                    caseList,
                    it.menu,
                    repository,
                    it.hologram
                )
            )
        }

    }


    fun getBoxes() = boxes

}