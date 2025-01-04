package ru.lewis.cases.model.animation.impl

import eu.decentsoftware.holograms.api.DHAPI
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import ru.lewis.cases.model.animation.AbstractAnimation
import ru.lewis.cases.model.box.ActiveBox
import ru.lewis.cases.model.casehandling.CaseData
import ru.lewis.cases.model.casehandling.Gift
import kotlin.math.cos
import kotlin.math.sin

class SpinAnimation(
    private val plugin: Plugin,
    private val position: Position,
    override val box: ActiveBox,
    override val player: Player,
    override val data: CaseData,
    override val gift: Gift
) : AbstractAnimation(player, box, data, gift) {

    private val rotationSpeed = Math.PI / 50
    private lateinit var center: Location
    private val radius = 1.5
    private var isCenter = false
    private var lastItem = -1

    init {
        AnimationThread(
            plugin, 15,
            { start() },
            { tickDelta -> onTickPeriod(tickDelta) },
            {
                onStop?.run()
                stop()
            }
        )

    }

    override fun start() {
        player.closeInventory()

        center = calculateCenter()
        val giftList = data.giftList
        val angleStep = (Math.PI * 2) / giftList.size

        giftList.forEachIndexed { index, currentGift ->
            val angle = index * angleStep
            val location = calculateLocation(angle)
            createHologram(currentGift, location)
        }
    }

    override fun onTickPeriod(tickDelta: Int) {
        val angleOffset = tickDelta * rotationSpeed
        val angleStep = (Math.PI * 2) / data.giftList.size
        val giftList = data.giftList

        HOLOGRAMS.forEachIndexed { index, hologram ->
            if (isCenter) return
            val angle = angleOffset + index * angleStep

            val thisItem = (angleOffset / angleStep).toInt() % giftList.size

            if (thisItem != lastItem) {
                center.world.playSound(center, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
                lastItem = thisItem
            }

            val location = calculateLocation(angle)

            if (giftList[index] == gift && angle >= Math.PI * 4 + 0.01) {
                center.world.createExplosion(location, 0f, false)
                isCenter = true
                return
            }

            DHAPI.moveHologram(hologram, location)
        }
    }

    override fun stop() {
        deleteHolograms()
    }

    private fun calculateCenter(): Location = box.location.clone().add(0.5, 1.0, 0.5)

    private fun calculateLocation(angle: Double): Location {
        return when (position) {
            Position.X_CORD -> {
                val x = center.x + radius * sin(angle)
                val y = center.y + radius * cos(angle)
                Location(center.world, x, y, center.z)
            }
            Position.Z_CORD -> {
                val z = center.z + radius * sin(angle)
                val y = center.y + radius * cos(angle)
                Location(center.world, center.x, y, z)
            }
        }
    }

    enum class Position {
        X_CORD,
        Z_CORD
    }
}