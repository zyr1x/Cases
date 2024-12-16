package ru.lewis.cases.service

import dev.rollczi.litecommands.adventure.LiteAdventureExtension
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory
import jakarta.inject.Inject
import jakarta.inject.Singleton
import me.lucko.helper.terminable.TerminableConsumer
import me.lucko.helper.terminable.module.TerminableModule
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import org.bukkit.plugin.Plugin
import ru.lewis.cases.commands.CaseGiveCommand
import ru.lewis.cases.commands.CaseResetCommand
import ru.lewis.cases.commands.ReloadCommand
import ru.lewis.cases.commands.argument.CaseDataArgument
import ru.lewis.cases.model.casehandling.CaseData

@Singleton
class CommandService @Inject constructor(
    private val plugin: Plugin,
    private val caseGiveCommand: CaseGiveCommand,
    private val caseResetCommand: CaseResetCommand,
    private val caseDataArgument: CaseDataArgument,
    private val reloadCommand: ReloadCommand
) : TerminableModule {

    @Suppress("UnstableApiUsage")
    override fun setup(consumer: TerminableConsumer) {
        LiteBukkitFactory.builder(plugin.name, plugin)

            .commands(caseGiveCommand, caseResetCommand, reloadCommand)

            .argument(CaseData::class.java, caseDataArgument)

            // extensions
            .extension(LiteAdventureExtension()) { config ->
                config.miniMessage(true)
                config.legacyColor(true)
                config.colorizeArgument(true)
                config.serializer(miniMessage())
            }
                .build()
    }
}