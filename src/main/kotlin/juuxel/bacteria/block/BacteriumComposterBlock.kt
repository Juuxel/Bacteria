package juuxel.bacteria.block

import juuxel.bacteria.BacteriumData
import juuxel.bacteria.block.entity.BacteriumComposterEntity
import juuxel.bacteria.item.BacteriumBunchItem
import juuxel.bacteria.lib.ModBlocks
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.state.StateFactory
import net.minecraft.state.property.IntegerProperty
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class BacteriumComposterBlock : BBlockWithEntity(FabricBlockSettings.copy(Blocks.COMPOSTER).ticksRandomly().build()) {
    override val name = "bacterium_composter"
    override val itemSettings: Nothing? = null
    override val blockEntityType = Companion.blockEntityType

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        builder.with(LEVEL)
    }

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult
    ): Boolean {
        val stack = player.mainHandStack
        val level = state[LEVEL]

        if (!stack.isEmpty && stack.item is BacteriumBunchItem && level < 4) {
            val entity = world.getBlockEntity(pos) as? BacteriumComposterEntity ?: return false
            entity.contents.add(BacteriumData.fromTag(stack.tag?.getCompound("BacteriumData") ?: return false))
            stack.subtractAmount(1)
            world.setBlockState(pos, state.with(LEVEL, level + 1))
        }

        return false
    }

    override fun afterBreak(
        world: World,
        player: PlayerEntity?,
        pos: BlockPos,
        state: BlockState?,
        be: BlockEntity?,
        stack: ItemStack?
    ) {
        super.afterBreak(world, player, pos, state, be, stack)

        if (be is BacteriumComposterEntity) {
            for (data in be.contents) {
                Block.dropStack(world, pos, data.toBunchItemStack())
            }
        }
    }

    override fun onRandomTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        if (!world.isClient && state[LEVEL] == 4) {
            val entity = world.getBlockEntity(pos) as? BacteriumComposterEntity ?: return
            world.setBlockState(pos, Blocks.COMPOSTER.defaultState)
            val data = combineBacteria(entity.contents)
            entity.contents.clear()
            Block.dropStack(world, pos, ItemStack(ModBlocks.colony, 1).apply {
                getOrCreateTag().put("BlockEntityTag", CompoundTag().apply {
                    put("BacteriumData", data.toTag())
                })
            })
        }
    }

    private fun combineBacteria(bacteria: List<BacteriumData>): BacteriumData {
        val lifetime = bacteria.map(BacteriumData::lifetime).average()
        val hunger = bacteria.map(BacteriumData::hunger).average()
        val type = bacteria.map(BacteriumData::type).random()

        return BacteriumData(lifetime, hunger, type, false)
    }

    companion object {
        val LEVEL = IntegerProperty.create("level", 1, 4)
        val blockEntityType = BlockEntityType(::BacteriumComposterEntity, null)
    }
}
