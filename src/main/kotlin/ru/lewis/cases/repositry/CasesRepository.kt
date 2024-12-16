package ru.lewis.cases.repositry

import com.google.inject.ImplementedBy
import org.bukkit.entity.Player
import ru.lewis.cases.model.casehandling.CaseData

@ImplementedBy(CasesRepositoryImpl::class)
interface CasesRepository {

    fun hasCase(case: CaseData, player: Player): Boolean
    fun setCase(case: CaseData, player: Player, count: Int)
    fun getCaseCount(case: CaseData, player: Player): Int
    fun reset(player: Player)

}