package ru.lewis.cases.model.menu.button

import net.kyori.adventure.text.minimessage.tag.resolver.Formatter
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
import ru.lewis.cases.model.menu.item.CustomAsyncItem
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
    private val player: Player,
    loadingItem: ItemTemplate
) : CustomAsyncItem(plugin, ItemBuilder(loadingItem.toItem())) {

    private var caseCount: Int? = null

    override fun onLoad(): ItemProvider {
        caseCount = repository.getCaseCount(data, player)
        return ItemBuilder(
            item.resolve(
                Formatter.number(
                    "count",
                    caseCount!!
                )
            ).toItem()
        )
    }

    override fun handleClick(p0: ClickType, player: Player, p2: InventoryClickEvent) {

        caseCount?.let {

            if (box.isOpen()) {
                player.playSound(
                    player.location,
                    Sound.ENTITY_VILLAGER_NO,
                    1.0f,
                    1.0f
                )
                return
            }

            if (it <= 0) {
                player.closeInventory()
                player.playSound(
                    player.location,
                    Sound.ENTITY_VILLAGER_NO,
                    1.0f,
                    1.0f
                )
                return
            }

            repository.setCase(data, player, it - 1)

            val gift = data.getRandomGift()
            val animation: AbstractAnimation = SpinAnimation(plugin, SpinAnimation.Position.X_CORD, box, player, data, gift)

            OpeningCase(
                player, box, gift, animation, data
            )

        }

    }

}