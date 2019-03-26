package juuxel.bacteria.block

import io.github.prospector.silk.block.SilkBlockWithEntity
import juuxel.bacteria.util.ModBlock
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.world.BlockView

abstract class BBlockWithEntity(settings: Block.Settings) : SilkBlockWithEntity(settings), ModBlock {
    abstract override val blockEntityType: BlockEntityType<*>

    final override fun createBlockEntity(view: BlockView?) = blockEntityType.instantiate()
}
