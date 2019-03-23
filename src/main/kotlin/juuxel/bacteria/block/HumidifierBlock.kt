package juuxel.bacteria.block

import juuxel.bacteria.block.entity.HumidifierEntity
import juuxel.bacteria.lib.ModContainers
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.VerticalEntityPosition
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import net.minecraft.world.World

class HumidifierBlock : BBlockWithEntity(FabricBlockSettings.copy(Blocks.BLAST_FURNACE).lightLevel(0).build()), InventoryProvider {
    override val name = "humidifier"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)
    override val blockEntityType = Companion.blockEntityType

    override fun activate(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult): Boolean {
        if (!world.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(ModContainers.humidifier, player) {
                it.writeBlockPos(pos)
            }
        }

        return true
    }

    override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, vep: VerticalEntityPosition?): VoxelShape = outlineShape
    override fun getRenderLayer() = BlockRenderLayer.TRANSLUCENT

    override fun getInventory(state: BlockState, world: IWorld, pos: BlockPos) =
        (world.getBlockEntity(pos) as? HumidifierEntity)?.getInventory()

    companion object {
        val blockEntityType = BlockEntityType(::HumidifierEntity, null)
        val outlineShape: VoxelShape =
            VoxelShapes.union(
                Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
                Block.createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
                Block.createCuboidShape(2.0, 2.0, 2.0, 14.0, 14.0, 14.0)
            )
    }
}
