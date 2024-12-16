package ru.lewis.cases.commands

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission
import jakarta.inject.Inject
import org.bukkit.Sound
import org.bukkit.entity.Player
import ru.lewis.cases.repositry.CasesRepository

@Command(name = "case", aliases = ["cases"])
@Permission("case.reset")
class CaseResetCommand @Inject constructor(
    private val repository: CasesRepository
){

    @Execute(name = "reset")
    fun execute(@Context sender: Player, @Arg target: Player) {

        repository.reset(target)

        sender.playSound(
            sender.location, Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f
        )

    }
}