package ru.lewis.cases.model.box

import eu.decentsoftware.holograms.api.DHAPI
import eu.decentsoftware.holograms.api.holograms.Hologram
import eu.decentsoftware.holograms.api.holograms.HologramLine
import me.lucko.helper.Events
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin
import ru.lewis.cases.configuration.type.MenuConfiguration
import ru.lewis.cases.configuration.type.MiniMessageComponent
import ru.lewis.cases.configuration.type.import
import ru.lewis.cases.extension.legacy
import ru.lewis.cases.model.casehandling.CaseData
import ru.lewis.cases.model.menu.CaseButton
import ru.lewis.cases.model.menu.NextPageButton
import ru.lewis.cases.model.menu.PreviousPageButton
import ru.lewis.cases.repositry.CasesRepository
import xyz.xenondevs.invui.window.Window
import java.util.UUID

class ActiveBox(
    val plugin: Plugin,
    val location: Location,
    val caseList: List<CaseData>,
    val menu: MenuConfiguration,
    val repository: CasesRepository,
    val hologramLines: List<MiniMessageComponent>
) {

    private var isOpening: Boolean = false
    private lateinit var hologram: Hologram

    init {
        Events.subscribe(PlayerInteractEvent::class.java)
            .filter { event ->
                event.action == Action.RIGHT_CLICK_BLOCK
            }
            .filter { event ->
                val blockLocation = event.clickedBlock?.location
                val blockX = blockLocation?.x
                val blockY = blockLocation?.y
                val blockZ = blockLocation?.z

                val boxX = location.x
                val boxY = location.y
                val boxZ = location.z

                blockX == boxX && blockY == boxY && blockZ == boxZ
            }
            .handler { event ->
                event.isCancelled = true
                this.openMenu(event.player)
            }

        createHologram()
    }

    private fun createHologram() {

        val hologramLocation = location.clone().add(0.5, hologramLines.size * 0.9, 0.5)
        hologram = DHAPI.createHologram(UUID.randomUUID().toString(), hologramLocation)

        val hologramPage = hologram.getPage(0)
        var i = 0.0
        hologramLines.forEach {
            hologramPage.addLine(
                HologramLine(hologramPage, hologramLocation.clone().add(0.0, i, 0.0), it.legacy())
            )
            i++
        }

    }

    private fun hideHologram() {
        hologram.delete()
    }

    private fun visibleHologram() {
        createHologram()
    }

    fun openMenu(player: Player) {
        val box = this

        Window.single().apply {

            import(menu.template) {

                addIngredient('<', PreviousPageButton(menu.buttons['<']?.toItem() ?: throw IllegalArgumentException("Конфигурация бокса настроена неправильно!")))
                addIngredient('>', NextPageButton(menu.buttons['>']?.toItem() ?: throw IllegalArgumentException("Конфигурация бокса настроена неправильно!")))

                menu.customItems.forEach { (c, itemTemplate) -> addIngredient(c, itemTemplate.toItem()) }

                this.setContent(
                    caseList.map {
                        CaseButton(
                            plugin,
                            it.item,
                            it,
                            box,
                            repository,
                            player
                        )
                    }
                )
            }
        }.open(player)
    }

    fun open() {
        toggle()
        hideHologram()
    }

    fun close() {
        toggle()
        visibleHologram()
    }

    private fun toggle() {
        isOpening = !isOpening
    }

    fun isOpen() = isOpening

}