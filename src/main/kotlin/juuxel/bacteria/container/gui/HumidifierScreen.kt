package juuxel.bacteria.container.gui

import juuxel.bacteria.block.entity.HumidifierEntity
import juuxel.bacteria.container.HumidifierContainer
import juuxel.bacteria.lib.Colors
import juuxel.bacteria.lib.ModContainers
import net.minecraft.entity.player.PlayerEntity
import kotlin.math.ceil

class HumidifierScreen(container: HumidifierContainer, player: PlayerEntity) :
    BacteriaContainerScreen<HumidifierContainer>(
        container,
        player,
        ModContainers.humidifier.path
    ) {

    override fun drawForeground(int_1: Int, int_2: Int) {
        super.drawForeground(int_1, int_2)
        val progressRatio = 66f / HumidifierEntity.MAX_PROGRESS.toFloat()
        drawOutline(59, 55, 58 + 69, 61, Colors.BLACK)
        drawRect(60, 56, 60 + ceil(progressRatio * container.propertyDelegate[0]).toInt(), 60, Colors.GREEN)

        val fluidRatio = 28f / HumidifierEntity.TANK_SIZE.toFloat()
        drawOutline(40, 30, 45, 60, Colors.BLUE)
        drawRect(41, 59 - ceil(fluidRatio * container.propertyDelegate[1]).toInt(), 44, 59, Colors.LIGHT_BLUE)
    }
}
