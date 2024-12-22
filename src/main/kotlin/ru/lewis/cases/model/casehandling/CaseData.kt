package ru.lewis.cases.model.casehandling

import org.bukkit.entity.Player
import ru.lewis.cases.configuration.type.ItemTemplate
import ru.lewis.cases.configuration.type.MiniMessageComponent
import ru.lewis.cases.model.box.ActiveBox
import ru.lewis.cases.model.animation.AbstractAnimation
import kotlin.random.Random

class CaseData(
    val id: Int,
    val giftList: List<Gift>,
    val item: ItemTemplate
) {

    fun open(player: Player, box: ActiveBox, gift: Gift, animation: AbstractAnimation): OpeningCase {
        return OpeningCase(
            player, box, gift, animation, this
        )
    }

    fun getRandomGift(): Gift {
        if (giftList.isEmpty()) {
            throw IllegalStateException("Список подарков пуст!")
        }

        return giftList[Random.nextInt(giftList.size)]
    }

}