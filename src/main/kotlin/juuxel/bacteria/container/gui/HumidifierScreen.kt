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
        val ratio = 66f / HumidifierEntity.MAX_PROGRESS.toFloat()
        drawRect(59, 55, 58 + 69, 56, Colors.BLACK)
        drawRect(59, 60, 58 + 69, 61, Colors.BLACK)
        drawRect(59, 55, 60, 61, Colors.BLACK)
        drawRect(58 + 68, 55, 58 + 69, 61, Colors.BLACK)
        drawRect(60, 56, 60 + ceil(ratio * container.propertyDelegate[0]).toInt(), 60, Colors.GREEN)
    }
}
