package ru.lewis.cases.model.menu.animation

import org.bukkit.Sound
import xyz.xenondevs.invui.animation.impl.AbstractAnimation

abstract class AbstractSoundAnimation(
    tickDelay: Int,
    sound: Boolean = true
): AbstractAnimation(tickDelay) {

    init {
        if (sound) {
            this.addShowHandler { frame: Int?, index: Int? ->
                for (viewer in this.currentViewers) {
                    viewer.playSound(viewer.location, Sound.BLOCK_GLASS_STEP, 0.6f, 1.0f)
                }
            }
        }
    }

}