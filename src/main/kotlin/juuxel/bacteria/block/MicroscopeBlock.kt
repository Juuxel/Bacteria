package juuxel.bacteria.block

import juuxel.bacteria.Bacteria
import juuxel.bacteria.lib.ModContainers
import juuxel.bacteria.util.ModBlock
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderLayer
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.VerticalEntityPosition
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class MicroscopeBlock : Block(Settings.copy(Blocks.STONE)), ModBlock {
    override val name = "microscope"
    override val itemSettings = Item.Settings().itemGroup(Bacteria.itemGroup)

    override fun activate(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult): Boolean {
        if (!world.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(ModContainers.microscope, player) {}
        }

        return true
    }

    override fun getOutlineShape(
        state: BlockState?, view: BlockView?, pos: BlockPos?, vep: VerticalEntityPosition?
    ): VoxelShape = outlineShape

    override fun getRenderLayer() = BlockRenderLayer.CUTOUT

    companion object {
        val outlineShape: VoxelShape = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0)
    }
}
