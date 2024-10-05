package ru.lewis.example.configuration.type

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import ru.lewis.example.extension.asMiniMessageComponent
import ru.lewis.example.extension.parseMiniMessage

class MiniMessageComponent(var rawString: String, parsedComponent: Component) : ComponentLike by parsedComponent {

    fun resolve(vararg tagResolvers: TagResolver): MiniMessageComponent {
        return rawString.parseMiniMessage(*tagResolvers).asMiniMessageComponent()
    }

    override fun toString(): String {
        return "MiniMessageComponent($rawString)"
    }
}
