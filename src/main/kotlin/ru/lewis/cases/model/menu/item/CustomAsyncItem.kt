package ru.lewis.cases.model.menu.item

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.impl.AbstractItem
import java.util.concurrent.atomic.AtomicReference

abstract class CustomAsyncItem(
    plugin: Plugin,
    private val loadingItem: ItemProvider
) : AbstractItem() {

    private val itemProviderRef = AtomicReference<ItemProvider>()

    override fun getItemProvider(): ItemProvider {
        return itemProviderRef.get() ?: loadingItem
    }

    init {
        itemProviderRef.set(loadingItem)

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            try {
                val loadedItem = onLoad()
                itemProviderRef.set(loadedItem)
                notifyWindows()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    abstract fun onLoad(): ItemProvider
}