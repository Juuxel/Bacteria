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
    private var progress = 0

    override fun render(i1: Int, i2: Int, f: Float) {
        drawBackground()
        super.render(i1, i2, f)
        drawMouseoverTooltip(i1, i2)
    }

    override fun drawForeground(int_1: Int, int_2: Int) {
        super.drawForeground(int_1, int_2)
        incProgress()
        val ratio = 66f / HumidifierEntity.MAX_PROGRESS.toFloat()
        drawRect(59, 55, 58 + 67, 56, Colors.LIGHT_BLUE)
        drawRect(59, 60, 58 + 67, 61, Colors.BLUE)
        drawGradientRect(59, 56, 59 + ceil(ratio * progress).toInt(), 60, Colors.LIGHT_BLUE, Colors.BLUE)
    }

    private fun incProgress() {
        progress++

        if (progress >= HumidifierEntity.MAX_PROGRESS)
            progress = 0
    }
}