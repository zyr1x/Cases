package ru.lewis.cases.model.menu.animation

class CustomRowAnimation(
    tickDelay: Int,
    sound: Boolean = true
) : AbstractSoundAnimation(tickDelay, sound) {

    private var row: Int? = null

    override fun handleFrame(p0: Int) {
        if (row == null) {
            row = this.height - 1
        }

        var currentRow = row ?: return

        while (currentRow >= 0) {
            var showedSomething = false
            for (x in 0 until this.width) {
                val index = this.convToIndex(x, currentRow)

                if (slots.contains(index)) {
                    this.show(index)
                    showedSomething = true
                }
            }
            currentRow -= 1
            row = currentRow

            if (showedSomething) {
                break
            }
        }

        if (currentRow < 0) {
            this.finish()
        }
    }
}