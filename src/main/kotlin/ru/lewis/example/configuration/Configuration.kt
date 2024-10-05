package ru.lewis.example.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.example.configuration.type.MiniMessageComponent
import ru.lewis.example.extension.asMiniMessageComponent

@ConfigSerializable
data class Configuration(
    val example: MiniMessageComponent = "<green>Example".asMiniMessageComponent()
) {


}