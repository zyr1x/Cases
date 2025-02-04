package ru.lewis.cases.commands

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission
import jakarta.inject.Inject
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.lewis.cases.model.casehandling.CaseData
import ru.lewis.cases.repositry.CasesRepository

@Command(name = "case", aliases = ["cases"])
@Permission("case.give")
class CaseGiveCommand @Inject constructor(
    private val repository: CasesRepository
){

    @Execute(name = "give")
    fun execute(@Arg target: Player, @Arg data: CaseData, @Arg amount: Int) {

        val count = repository.getCaseCount(data, target) + amount
        repository.setCase(data, target, count)

    }

}