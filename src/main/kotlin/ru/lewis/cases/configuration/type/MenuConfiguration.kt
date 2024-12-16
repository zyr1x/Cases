package ru.lewis.cases.configuration.type

import org.bukkit.Material
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.cases.extension.asMiniMessageComponent

@ConfigSerializable
data class MenuConfiguration(
    val template: MenuConfig = MenuConfig(
        "Кейсы".asMiniMessageComponent(),
        structure = listOf(
            ". . . . . . . . .",
            ". . . . . . . . .",
            "x x x < x > x x x"
        )
    ),
    val buttons: Map<Char, ItemTemplate> = mapOf(
        '>' to ItemTemplate(Material.ARROW, displayName = "<red>Следующая страница".asMiniMessageComponent()),
        '<' to ItemTemplate(Material.ARROW, displayName = "<red>Предыдущая страница".asMiniMessageComponent())
    ),
    val customItems: Map<Char, ItemTemplate> = mapOf(
        'x' to ItemTemplate(Material.MAGENTA_STAINED_GLASS_PANE)
    )
)