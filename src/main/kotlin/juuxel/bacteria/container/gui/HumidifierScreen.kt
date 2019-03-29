package juuxel.bacteria.container.gui

import io.github.juuxel.polyester.container.screen.PolyesterScreen
import juuxel.bacteria.block.entity.HumidifierEntity
import juuxel.bacteria.container.HumidifierContainer
import juuxel.bacteria.lib.Colors
import juuxel.bacteria.lib.ModContainers
import net.minecraft.entity.player.PlayerEntity
import kotlin.math.ceil

class HumidifierScreen(container: HumidifierContainer, player: PlayerEntity) :
    PolyesterScreen<HumidifierContainer>(
        container,
        player,
        ModContainers.humidifier
    ) {

    override fun drawForeground(int_1: Int, int_2: Int) {
        super.drawForeground(int_1, int_2)
        val progressRatio = 66f / HumidifierEntity.MAX_PROGRESS.toFloat()
        drawOutline(59, 55, 58 + 69, 61, Colors.BLACK)
        drawRect(60, 56, 60 + ceil(progressRatio * container.propertyDelegate[0]).toInt(), 60, Colors.GREEN)

        val fluidRatio = 58f / HumidifierEntity.TANK_SIZE.toFloat()
        drawOutline(140, 15, 155, 75, Colors.BLUE)
        drawRect(141, 74 - ceil(fluidRatio * container.propertyDelegate[1]).toInt(), 154, 74, Colors.LIGHT_BLUE)
    }
}
