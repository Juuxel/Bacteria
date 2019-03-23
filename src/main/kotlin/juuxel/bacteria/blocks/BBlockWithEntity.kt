package juuxel.bacteria.blocks

import juuxel.bacteria.util.ModBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.world.BlockView

abstract class BBlockWithEntity(settings: Block.Settings) : BlockWithEntity(settings), ModBlock {
    abstract override val blockEntityType: BlockEntityType<*>

    override fun getRenderType(state: BlockState?) = BlockRenderType.MODEL
    final override fun createBlockEntity(view: BlockView?) = blockEntityType.instantiate()
}
