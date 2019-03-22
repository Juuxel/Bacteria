package juuxel.bacteria.blocks

import juuxel.bacteria.Bacteria
import juuxel.bacteria.items.BacteriumBunchItem
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.text.TranslatableTextComponent
import net.minecraft.util.Hand
import net.minecraft.util.Tickable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry
import net.minecraft.world.BlockView
import net.minecraft.world.World

class ColonyBlock : BlockWithEntity(FabricBlockSettings.copy(Blocks.SPONGE).dropsNothing().build()) {
    override fun getRenderType(state: BlockState?) = BlockRenderType.MODEL
    override fun createBlockEntity(view: BlockView) = Bacteria.colonyBEType.instantiate()

    @Deprecated("Mojang is weird")
    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): Boolean {
        val upState = world.getBlockState(pos.up())

        if (isValidTargetBlock(upState)) {
            (world.getBlockEntity(pos) as? Entity)?.let { entity ->
                entity.target = upState.block
            }
            world.playSound(player, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCK, 1f, 1f)
        } else {
            world.playSound(player, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCK, 0.6f, 1f)
            player.addChatMessage(TranslatableTextComponent("chat.bacteria.invalid_block"), true)
        }

        return true
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
        Block.dropStack(
            world,
            pos,
            ItemStack(Bacteria.bacteriumBunch, (0..3).random()).apply {
                getOrCreateTag().put(
                    "BacteriumData",
                    BacteriumBunchItem.Data(
                        // TODO: Bacterium data and evolution
                    ).toNbt()
                )
            }
        )
    }

    companion object {
        private fun isValidTargetBlock(state: BlockState) =
            !state.isAir && !state.block.matches(Bacteria.inedibleTag)
    }

    class Entity : BlockEntity(Bacteria.colonyBEType), Tickable {
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
                    world.setBlockState(offsetPos, Bacteria.colonyBlock.defaultState)
                    (world.getBlockEntity(offsetPos) as? Entity)?.let {
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
}