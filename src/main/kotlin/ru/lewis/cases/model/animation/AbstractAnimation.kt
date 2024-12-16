package ru.lewis.cases.model.animation

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import ru.lewis.cases.model.box.ActiveBox
import ru.lewis.cases.model.casehandling.CaseData
import ru.lewis.cases.model.casehandling.Gift
import java.util.function.Consumer

abstract class AbstractAnimation(
    protected open val player: Player,
    protected open val box: ActiveBox,
    protected open val data: CaseData,
    protected open val gift: Gift
) {

    protected var onStop: Runnable? = null

    fun setStopAction(runnable: Runnable) {
        onStop = runnable
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
            this.runTaskTimer(plugin, 1L, 1L)
        }

        private val TICK: Int = 20
        private var tickChanging: Int = TICK

        override fun run() {

            if (tickChanging == 0) {
                tickChanging = TICK
                timeInSeconds--
            }

            if (timeInSeconds == 0) {
                println("делаю onStop()")
                this.onStop.run()
                this.cancel()
                return
            }

            tickChanging--
            onTickDelay.accept(timeInSeconds)

        }

    }
}