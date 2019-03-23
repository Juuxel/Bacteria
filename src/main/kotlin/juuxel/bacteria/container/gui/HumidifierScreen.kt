package juuxel.bacteria.container.gui

import juuxel.bacteria.block.entity.HumidifierEntity
import juuxel.bacteria.container.HumidifierContainer
import juuxel.bacteria.lib.Colors
import juuxel.bacteria.lib.ModContainers
import juuxel.bacteria.lib.ModPackets
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.util.SystemUtil
import net.minecraft.util.math.BlockPos
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

        if (System.currentTimeMillis() % 100L == 0L)
            ClientSidePacketRegistry.INSTANCE.sendToServer(ModPackets.createRequestHumidifierProgress(container.pos))
    }

    override fun drawForeground(int_1: Int, int_2: Int) {
        super.drawForeground(int_1, int_2)
        val ratio = 66f / HumidifierEntity.MAX_PROGRESS.toFloat()
        drawRect(58, 55, 58 + 68, 56, Colors.BLUE)
        drawRect(58, 60, 58 + 68, 61, Colors.BLUE)
        drawRect(59, 56, 59 + ceil(ratio * progress).toInt(), 60, Colors.BLUE)
    }

    fun updateProgress(progress: Int) {
        this.progress = progress
    }
}