package ru.lewis.cases.model.animation

import eu.decentsoftware.holograms.api.DHAPI
import eu.decentsoftware.holograms.api.holograms.Hologram
import eu.decentsoftware.holograms.api.holograms.HologramLine
import eu.decentsoftware.holograms.api.utils.items.HologramItem
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import ru.lewis.cases.configuration.type.ItemTemplate
import ru.lewis.cases.configuration.type.MiniMessageComponent
import ru.lewis.cases.extension.legacy
import ru.lewis.cases.model.box.ActiveBox
import ru.lewis.cases.model.casehandling.CaseData
import ru.lewis.cases.model.casehandling.Gift
import java.util.*
import java.util.function.Consumer

abstract class AbstractAnimation(
    protected open val player: Player,
    protected open val box: ActiveBox,
    protected open val data: CaseData,
    protected open val gift: Gift
) {

    protected var onStop: Runnable? = null
    protected val HOLOGRAMS: MutableList<Hologram> = mutableListOf()

    fun setStopAction(runnable: Runnable) {
        onStop = runnable
    }

    protected fun createHologram(gift: Gift, location: Location) {
        val giftName = gift.name
        val itemTemplate = gift.itemTemplate
        val item = itemTemplate.toItem()
        val hologram = DHAPI.createHologram(UUID.randomUUID().toString(), location)
        val hologramPage = hologram.getPage(0)
        val hologramItem = HologramItem.fromItemStack(item)
        val hologramLineTwo: HologramLine?

        when (hologramItem.material) {
            Material.PLAYER_HEAD -> {
                hologramLineTwo = HologramLine(hologramPage, location, "#SMALLHEAD:" + hologramItem.content)
            }
            else -> {
                hologramLineTwo = HologramLine(hologramPage, location, "#ICON:" + hologramItem.content)
            }
        }

        giftName.let {
            val hologramLineOne = HologramLine(hologramPage, location, it.asComponent().legacy())
            hologramPage.addLine(hologramLineOne)
        }

        hologramPage.addLine(hologramLineTwo)
        HOLOGRAMS.add(hologram)
    }

    protected fun deleteHolograms() {
        HOLOGRAMS.forEach { DHAPI.removeHologram(it.name) }
        HOLOGRAMS.clear()
    }

    protected abstract fun start()
    abstract fun stop()
    abstract fun onTickPeriod(tickDelta: Int)

    protected inner class AnimationThread(
        plugin: Plugin,
        private var timeInSeconds: Int,
        private val onStart: Runnable,
        private val onTickDelay: Consumer<Int>,
        private val onStop: Runnable) : BukkitRunnable() {

        init {
            this.onStart.run()
            this.runTaskTimer(plugin, 0L, 1L)
        }

        private var tickChanging: Int = 0

        override fun run() {

            if (tickChanging % 20 == 0) {
                timeInSeconds--
            }

            if (timeInSeconds == 0) {
                this.onStop.run()
                this.cancel()
                return
            }

            tickChanging++
            onTickDelay.accept(tickChanging)

        }

    }
}