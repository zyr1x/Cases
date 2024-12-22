package ru.lewis.cases.configuration

import org.bukkit.Material
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.cases.configuration.type.*
import ru.lewis.cases.extension.asMiniMessageComponent

@ConfigSerializable
data class Configuration(
    val boxList: List<Box> = listOf(
        Box()
    ),
    val database: DatabaseConfiguration = DatabaseConfiguration()
) {

    @ConfigSerializable
    data class Box(
        val id: Int = 0,
        val menu: MenuConfiguration = MenuConfiguration(),
        val location: LocationConfiguration = LocationConfiguration(
            6.0, -19.0, 4.0, 90f, 0f, "spawn"
        ),
        val caseList: List<Case> = listOf(
            Case()
        ),
        val hologram: List<MiniMessageComponent> = listOf(
            "<red>Box".asMiniMessageComponent(),
            "<rainbow>Click for open!".asMiniMessageComponent()
        )
    ) {

        @ConfigSerializable
        data class Case(
            val id: Int = 1,
            val item: ItemTemplate = ItemTemplate(Material.CHEST),
            val gifts: List<Gift> = listOf(
                Gift()
            )
        ) {

            @ConfigSerializable
            data class Gift(
                val name: MiniMessageComponent = "Смерть".asMiniMessageComponent(),
                val chance: Int = 50,
                val item: ItemTemplate = ItemTemplate(Material.DIAMOND_SWORD),
                val title: TitleConfiguration = TitleConfiguration("WOW!", "да ты крут..."),
                val commandsOnConsole: List<String>? = null,
                val items: List<ItemTemplate>? = null
            )
        }

    }


}