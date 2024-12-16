package ru.lewis.cases.configuration.type

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.title.Title
import net.kyori.adventure.util.Ticks
import org.bukkit.entity.Player
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.cases.extension.parseMiniMessage
import java.time.Duration

@ConfigSerializable
data class TitleConfiguration(
    val title: String,
    val subtitle: String,
    val durationFadeIn: Duration = Ticks.duration(10),
    val durationStay: Duration = Ticks.duration(70),
    val durationFadeOut: Duration = Ticks.duration(20),
) {

    fun createTitle(vararg tagResolvers: TagResolver): Title = Title.title(
        title.parseMiniMessage(*tagResolvers),
        subtitle.parseMiniMessage(*tagResolvers),
        Title.Times.times(durationFadeIn, durationStay, durationFadeOut)
    )

    fun show(player: Player, vararg tagResolvers: TagResolver) {
        player.showTitle(createTitle(*tagResolvers))
    }
}
