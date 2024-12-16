package ru.lewis.cases.model.casehandling

import org.bukkit.entity.Player
import ru.lewis.cases.model.box.ActiveBox
import ru.lewis.cases.model.animation.AbstractAnimation

class OpeningCase(
    private val player: Player,
    private val box: ActiveBox,
    private val gift: Gift,
    private val animation: AbstractAnimation,
    private val data: CaseData
) {

    private var isOpening: Boolean = false

    init {
        isOpening = true
        animation.setStopAction { stop() }
        box.open()
    }

    private fun givePrize() {
        gift.give(player)
    }

    private fun stop() {
        isOpening = false
        givePrize()
        box.close()
    }

    fun isOpening(): Boolean = isOpening

}