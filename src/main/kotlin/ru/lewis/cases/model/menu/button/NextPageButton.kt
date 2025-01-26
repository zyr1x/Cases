package ru.lewis.cases.model.menu.button

import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.controlitem.PageItem

class NextPageButton(
    private val item: ItemStack
) : PageItem(true) {
    override fun getItemProvider(p0: PagedGui<*>?): ItemProvider {
        return ItemBuilder(item)
    }

}