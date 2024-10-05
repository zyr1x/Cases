package ru.lewis.example

import com.google.inject.Inject
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.Plugin
import org.slf4j.Logger
import ru.lewis.example.model.SmartLifoCompositeTerminable
import ru.lewis.example.model.listener.BlockBreakListener
import ru.lewis.example.model.listener.EntityDeathListener
import ru.lewis.example.model.placeholder.CustomPlaceHolder
import ru.lewis.example.service.CommandService
import ru.lewis.example.service.ConfigurationService
import ru.lewis.example.service.LevelService
import xyz.xenondevs.invui.InvUI

class Main @Inject constructor(
    private val bukkitAudiences: BukkitAudiences,
    private val configurationService: ConfigurationService,
    private val commandService: CommandService,
    private val levelService: LevelService,
    private val customPlaceHolder: CustomPlaceHolder,
    private val blockBreakListener: BlockBreakListener,
    private val entityDeathListener: EntityDeathListener,
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
            bindModule(levelService)
        }

        plugin.server.pluginManager.registerEvents(blockBreakListener, plugin)
        plugin.server.pluginManager.registerEvents(entityDeathListener, plugin)

        customPlaceHolder.register()
    }

    fun stop() {
        terminableRegistry.closeAndReportException()
        customPlaceHolder.unregister()
    }

    companion object {
        lateinit var audiences: BukkitAudiences
    }
}
