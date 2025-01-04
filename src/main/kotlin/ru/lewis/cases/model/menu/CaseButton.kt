package ru.lewis.cases.model.menu

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.plugin.Plugin
import ru.lewis.cases.configuration.type.ItemTemplate
import ru.lewis.cases.model.animation.AbstractAnimation
import ru.lewis.cases.model.animation.impl.SpinAnimation
import ru.lewis.cases.model.box.ActiveBox
import ru.lewis.cases.model.casehandling.CaseData
import ru.lewis.cases.model.casehandling.OpeningCase
import ru.lewis.cases.repositry.CasesRepository
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class CaseButton(
    private val plugin: Plugin,
    private val item: ItemTemplate,
    private val data: CaseData,
    private val box: ActiveBox,
    private val repository: CasesRepository,
    private val player: Player
) : AbstractItem() {

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(
            item.resolve(
                Placeholder.unparsed(
                    "count",
                    repository.getCaseCount(data, player).toString()
                )
            ).toItem()
        )
    }

    override fun handleClick(p0: ClickType, player: Player, p2: InventoryClickEvent) {

        if (box.isOpen()) {
            player.playSound(
                player.location,
                Sound.ENTITY_VILLAGER_NO,
                1.0f,
                1.0f
            )
            return
        }

        if (!repository.hasCase(data, player)) {
            player.closeInventory()
            player.playSound(
                player.location,
                Sound.ENTITY_VILLAGER_NO,
                1.0f,
                1.0f
            )
            return
        }

        val count = repository.getCaseCount(data, player) - 1
        repository.setCase(data, player, count)

        val gift = data.getRandomGift()
        val animation: AbstractAnimation = SpinAnimation(plugin, SpinAnimation.Position.Z_CORD, box, player, data, gift)

        OpeningCase(
            player, box, gift, animation, data
        )

    }

}