package ru.lewis.cases.commands

import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import jakarta.inject.Inject
import org.bukkit.Sound
import org.bukkit.entity.Player
import ru.lewis.cases.service.ConfigurationService

@Command(name = "case", aliases = ["cases"])
class ReloadCommand @Inject constructor(
    private val configurationService: ConfigurationService
) {

    @Execute(name = "reload")
    fun execute(@Context sender: Player) {
        configurationService.reload()

        sender.playSound(
            sender.location, Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f
        )
    }
}