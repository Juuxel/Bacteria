package juuxel.bacteria.block.entity

import juuxel.bacteria.Bacteria
import juuxel.bacteria.BacteriumData
import juuxel.bacteria.block.ColonyBlock
import juuxel.bacteria.lib.ModBlocks
import juuxel.bacteria.lib.ModItems
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.Waterloggable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.state.property.Properties
import net.minecraft.util.Tickable
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry
import kotlin.math.roundToInt

class ColonyEntity : BlockEntity(ColonyBlock.blockEntityType), Tickable {
    internal var target: Block? = null
    private var age = 0
    private var hasEaten = false
    private var data = BacteriumData.default

    override fun tick() {
        if (world.isClient) return

        val lifetimeRandomMax = (16 * data.lifetime).roundToInt()
        if (target != null && world.random.nextInt(lifetimeRandomMax) == 0) {
            age++
        }

        if (age >= 3 || world.gameRules.getBoolean(Bacteria.jamColoniesGameRule)) {
            if (!hasEaten && world.random.nextInt(16) == 0) {
                dropItems()
            }

            world.clearBlockState(pos)
            return
        }

        Direction.values().forEach { direction ->
            val offsetPos = pos.offset(direction)
            val state = world.getBlockState(offsetPos)
            val randomMax = (16 / (data.hunger / 0.5)).roundToInt()
            if (world.random.nextInt(randomMax) == 0 && matchesTarget(state)) {
                world.setBlockState(offsetPos, ModBlocks.colony.defaultState)
                hasEaten = true
                (world.getBlockEntity(offsetPos) as? ColonyEntity)?.let {
                    it.target = target

                    // Mutation
                    if (world.random.nextInt(4) == 0) {
                        val newLifetime = data.lifetime + world.random.nextDouble() * 0.3
                        val newHunger = data.hunger + world.random.nextDouble() * 0.3

                        val newType =
                            if (world.random.nextInt(6) == 0)
                                BacteriumData.Type.values().random()
                            else data.type

                        it.data = BacteriumData(newLifetime, newHunger, newType, false)
                    }
                }
            }
        }
    }

    fun dropItems() {
        Block.dropStack(
            world, pos, data.toBunchItemStack(amount = (1..2).random())
        )
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
        putBoolean("HasEaten", hasEaten)
        put("BacteriumData", data.toTag())
    }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)

        if (tag.containsKey("Target")) {
            target = Registry.BLOCK[tag.getInt("Target")]
        }

        age = tag.getInt("Age")
        hasEaten = tag.getBoolean("HasEaten")
        data = BacteriumData.fromTag(tag.getCompound("BacteriumData"))
    }
}
