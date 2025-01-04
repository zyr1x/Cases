package ru.lewis.cases.model.animation.impl

import eu.decentsoftware.holograms.api.DHAPI
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import ru.lewis.cases.model.animation.AbstractAnimation
import ru.lewis.cases.model.box.ActiveBox
import ru.lewis.cases.model.casehandling.CaseData
import ru.lewis.cases.model.casehandling.Gift

class ChristmasSpinAnimation(
    private val plugin: Plugin,
    override val box: ActiveBox,
    override val player: Player,
    override val data: CaseData,
    override val gift: Gift
) : AbstractAnimation(player, box, data, gift) {

    private val center = box.location.clone().add(0.5, 1.5, 0.5) // Центр вращения (над сундуком)
    private val radius = 2.0 // Радиус вращения
    private var currentSpeed = 0.4 // Начальная скорость вращения (радианы за тик)
    private var angle = 0.0 // Текущий угол вращения
    private var remainingTicks = 200

    init {
        AnimationThread(
            plugin, 10,
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

        box.location.world.playSound(
            box.location,
            Sound.BLOCK_ENDER_CHEST_OPEN,
            1.0f,
            1.0f
        )

        // Расчет начальных позиций голограмм вокруг центра по вертикали
        val initialAngleStep = 2 * Math.PI / data.giftList.size
        val verticalStep = 1.0 // Расстояние между предметами по вертикали
        data.giftList.forEachIndexed { index, currentGift ->
            val itemAngle = index * initialAngleStep
            val x = radius * Math.cos(itemAngle)
            val z = radius * Math.sin(itemAngle)
            val y = 1.5 + index * verticalStep // Вертикальное распределение

            val hologramLocation = center.clone().add(x, y, z)

            createHologram(currentGift, hologramLocation)
        }
    }

    override fun onTickPeriod(tickDelta: Int) {
        if (remainingTicks <= 0) {
            stop()
            return
        }

        // Уменьшаем оставшиеся тики
        remainingTicks--

        // Скорость уменьшается с течением времени
        currentSpeed = (0.1 + 0.3 * (remainingTicks / 200.0))

        // Обновляем позиции голограмм по вертикали
        HOLOGRAMS.forEachIndexed { index, hologram ->
            val itemAngle = angle + (index * (2 * Math.PI / HOLOGRAMS.size))
            val x = radius * Math.cos(itemAngle)
            val z = radius * Math.sin(itemAngle)
            val y = 1.5 + index * 1.0 // Увеличиваем вертикальное положение

            // Перемещаем голограмму
            DHAPI.moveHologram(hologram, center.clone().add(x, y, z))

            // Добавляем частицы вокруг голограммы
            player.world.spawnParticle(
                Particle.REDSTONE, // Тип частицы
                center.clone().add(x, y, z), // Позиция вокруг которой появляются частицы
                0, // Количество частиц (если 0, то будет 1)
                0.1, 0.1, 0.1, 0.1, // Разброс частиц по осям (скорость и направление)
                Particle.DustOptions(Color.fromRGB(255, 255, 0), 1.0f) // Цвет и размер частиц (желтые)
            )

        }

        // Увеличиваем угол для следующего тика
        angle += currentSpeed

        // Остановка на последнем тике
        if (remainingTicks == 1) {
            HOLOGRAMS.forEachIndexed { index, hologram ->
                if (data.giftList[index] == gift) {
                    DHAPI.moveHologram(hologram, center.clone().add(0.0, 1.0, 0.0))
                } else {
                    hologram.delete()
                }
            }
        }
    }


    override fun stop() {
        box.location.world.playSound(
            box.location,
            Sound.BLOCK_ENDER_CHEST_CLOSE,
            1.0f,
            1.0f
        )

        deleteHolograms()
    }

}