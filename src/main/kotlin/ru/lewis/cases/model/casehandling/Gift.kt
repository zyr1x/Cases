package ru.lewis.cases.model.casehandling

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ru.lewis.cases.configuration.type.ItemTemplate
import ru.lewis.cases.configuration.type.MiniMessageComponent
import ru.lewis.cases.configuration.type.TitleConfiguration

class Gift(
    val name: MiniMessageComponent,
    private val chance: Int,
    val itemTemplate: ItemTemplate,
    val title: TitleConfiguration,
    private val itemList: List<ItemTemplate>? = null,
    private val commandsOnConsole: List<String>? = null
) {

    fun give(player: Player) {
        itemList?.let {
            it.forEach { itemTemplate ->
                player.inventory.addItem(itemTemplate.toItem())
            }
        }
        commandsOnConsole?.let {
            it.forEach { command ->
                Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    command.replace("<player>", player.name)
                )
            }
        }
        title.show(player, Placeholder.unparsed("chance", chance.toString()))
    }

}