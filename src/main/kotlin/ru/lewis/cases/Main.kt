package ru.lewis.cases

import com.google.inject.Inject
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.Plugin
import org.slf4j.Logger
import ru.lewis.cases.model.SmartLifoCompositeTerminable
import ru.lewis.cases.model.box.BoxManager
import ru.lewis.cases.repositry.CasesRepository
import ru.lewis.cases.repositry.CasesRepositoryImpl
import ru.lewis.cases.service.CommandService
import ru.lewis.cases.service.ConfigurationService
import xyz.xenondevs.invui.InvUI

class Main @Inject constructor(
    private val bukkitAudiences: BukkitAudiences,
    private val configurationService: ConfigurationService,
    private val casesRepositoryImpl: CasesRepositoryImpl,
    private val commandService: CommandService,
    private val boxManager: BoxManager,
    private val plugin: Plugin,
    logger: Logger,
    ) {

    private val terminableRegistry = SmartLifoCompositeTerminable(logger)

    fun start() {

        InvUI.getInstance().setPlugin(plugin)

        audiences = bukkitAudiences
        terminableRegistry.apply {
            with(bukkitAudiences)
            bindModule(configurationService)
            bindModule(commandService)
            bindModule(boxManager)
            bindModule(casesRepositoryImpl)
        }
    }

    fun stop() {
        terminableRegistry.closeAndReportException()
    }

    companion object {
        lateinit var audiences: BukkitAudiences
    }
}
