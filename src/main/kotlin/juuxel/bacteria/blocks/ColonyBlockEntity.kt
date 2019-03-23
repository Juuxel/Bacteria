package juuxel.bacteria.blocks

import juuxel.bacteria.Bacteria
import juuxel.bacteria.lib.ModBlocks
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.Waterloggable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.state.property.Properties
import net.minecraft.util.Tickable
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry

class ColonyBlockEntity : BlockEntity(ColonyBlock.blockEntityType), Tickable {
    internal var target: Block? = null
    private var age = 0

    override fun tick() {
        if (world.isClient) return

        if (target != null && world.random.nextInt(16) == 0) {
            age++

            if (age >= 3) {
                world.clearBlockState(pos)
            }
        }

        if (world.gameRules.getBoolean(Bacteria.jamColoniesGameRule)) return

        Direction.values().forEach { direction ->
            val offsetPos = pos.offset(direction)
            val state = world.getBlockState(offsetPos)
            if (world.random.nextInt(16) == 0 && matchesTarget(state)) {
                world.setBlockState(offsetPos, ModBlocks.colony.defaultState)
                (world.getBlockEntity(offsetPos) as? ColonyBlockEntity)?.let {
                    it.target = target

                    /*// Mutation
                    if (world.random.nextInt(64) == 0) {
                        it.target = Direction.values().mapNotNull { d ->
                            val state = world.getBlockState(offsetPos.offset(d))
                            if (isValidTargetBlock(state))
                                state.block
                            else null
                        }.run {
                            if (isEmpty())
                                return
                            else random()
                        }
                    }*/
                }
            }
        }
    }

    private fun matchesTarget(state: BlockState) =
        state.block == target ||
            (target == Blocks.WATER && state.block is Waterloggable && state.material.isReplaceable &&
                state[Properties.WATERLOGGED])

    override fun toTag(tag: CompoundTag): CompoundTag = super.toTag(tag).apply {
        if (target != null) {
            putInt("Target", Registry.BLOCK.getRawId(target))
        }

        putInt("Age", age)
    }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)

        if (tag.containsKey("Target")) {
            target = Registry.BLOCK[tag.getInt("Target")]
        }

        age = tag.getInt("Age")
    }
}
