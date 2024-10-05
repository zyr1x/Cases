package ru.lewis.example.bootstrap

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.assistedinject.FactoryModuleBuilder
import jakarta.inject.Singleton
import me.lucko.helper.plugin.HelperPlugin
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import org.slf4j.Logger
import ru.lewis.example.bootstrap.assistedinject.Factory

class InjectionModule(
    private val helperPlugin: HelperPlugin
) : AbstractModule() {

    override fun configure() {

        bind(Plugin::class.java).toInstance(helperPlugin)

    }

    @Singleton
    @Provides
    fun BukkitAudiences(plugin: Plugin): BukkitAudiences = BukkitAudiences.create(plugin)

    @Provides
    fun MiniMessage(): MiniMessage = MiniMessage.miniMessage()

    @Provides
    fun Server(plugin: Plugin): Server = plugin.server

    @Provides
    fun Logger(plugin: Plugin): Logger = plugin.slF4JLogger

}
