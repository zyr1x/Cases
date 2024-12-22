package ru.lewis.cases.commands.argument

import dev.rollczi.litecommands.argument.Argument
import dev.rollczi.litecommands.argument.parser.ParseResult
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.suggestion.SuggestionContext
import dev.rollczi.litecommands.suggestion.SuggestionResult
import jakarta.inject.Inject
import org.bukkit.command.CommandSender
import ru.lewis.cases.model.box.BoxManager
import ru.lewis.cases.model.casehandling.CaseData
import ru.lewis.cases.service.ConfigurationService

class CaseDataArgument @Inject constructor(
    private val configurationService: ConfigurationService,
    private val boxManager: BoxManager
): ArgumentResolver<CommandSender, CaseData>() {

    private val boxList get() = configurationService.config.boxList

    override fun parse(p0: Invocation<CommandSender>?, p1: Argument<CaseData>?, id: String?): ParseResult<CaseData> {
        val caseData = boxManager.getBoxes().firstNotNullOfOrNull { it.caseList.find { case -> case.id.toString() == id } }
        return ParseResult.success(caseData)
    }

    override fun suggest(
        invocation: Invocation<CommandSender>?,
        argument: Argument<CaseData>?,
        context: SuggestionContext?
    ): SuggestionResult {
        val casesId = boxList.flatMap { box -> box.caseList.map { it.id } }.map { it.toString() }
        return SuggestionResult.of(casesId)
    }

}