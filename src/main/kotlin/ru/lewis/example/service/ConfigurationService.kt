package ru.lewis.example.service

import com.google.inject.Inject
import com.google.inject.Singleton
import me.lucko.helper.terminable.TerminableConsumer
import me.lucko.helper.terminable.module.TerminableModule
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.slf4j.Logger
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.kotlin.extensions.set
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.yaml.NodeStyle
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import ru.lewis.example.configuration.serializer.*
import ru.lewis.example.configuration.type.BossBarConfiguration
import ru.lewis.example.configuration.Configuration
import ru.lewis.example.configuration.type.MiniMessageComponent
import java.awt.Color
import java.time.Duration
import kotlin.io.path.*

@Singleton
class ConfigurationService @Inject constructor(
    private val plugin: Plugin,
    private val durationSerializer: DurationSerializer,
    private val miniMessageComponentSerializer: MiniMessageComponentSerializer,
    private val colorSerializer: ColorSerializer,
    private val potionEffectSerializer: PotionEffectSerializer,
    private val bossBarConfigurationSerializer: BossBarConfigurationSerializer,
    private val logger: Logger,
    private val potionEffectTypeSerializer: PotionEffectTypeSerializer,
    private val materialSerializer: MaterialSerializer,
    private val entityTypeSerializer: EntityTypeSerializer,
    private val enchantmentSerializer: EnchantmentSerializer
) : TerminableModule {

    lateinit var config: Configuration
        private set

    private val rootDirectory = Path("")
    private val configFile = plugin.dataFolder.toPath().resolve("config.yml")

    override fun setup(consumer: TerminableConsumer) = doReload()

    fun reload() = doReload()

    fun saveConfig() {

        createLoaderBuilder().path(configFile).build().let {
            it.save(it.createNode().set(config))
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    @Synchronized
    private fun doReload() {
        plugin.dataFolder.toPath().createDirectories()

        config = createLoaderBuilder().path(configFile).build().getAndSave<Configuration>()

    }

    private fun createLoaderBuilder(): YamlConfigurationLoader.Builder {
        return YamlConfigurationLoader.builder()

            .defaultOptions {
                it.serializers { serializers ->
                    serializers
                        .register(MiniMessageComponent::class.java, miniMessageComponentSerializer)
                        .register(Duration::class.java, durationSerializer)
                        .register(Color::class.java, colorSerializer)
                        .register(PotionEffect::class.java, potionEffectSerializer)
                        .register(BossBarConfiguration::class.java, bossBarConfigurationSerializer)
                        .register(Material::class.java, materialSerializer)
                        .register(PotionEffectType::class.java, potionEffectTypeSerializer)
                        .register(EntityType::class.java, entityTypeSerializer)
                        .register(Enchantment::class.java, enchantmentSerializer)
                        .registerAnnotatedObjects(objectMapperFactory())
                }
            }

            .indent(2)
            .nodeStyle(NodeStyle.BLOCK)
    }

    private inline fun <reified T : Any> YamlConfigurationLoader.getAndSave(): T {
        val obj = this.load().get(T::class)!!
        this.save(this.createNode().set(T::class, obj))
        return obj
    }

    //powered by BlackBoroness

}

