package juuxel.bacteria.block

import juuxel.bacteria.block.entity.ColonyEntity
import juuxel.bacteria.item.BacteriumBunchItem
import juuxel.bacteria.lib.ModItems
import juuxel.bacteria.lib.ModTags
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.TranslatableTextComponent
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ColonyBlock : BBlockWithEntity(FabricBlockSettings.copy(Blocks.SPONGE).dropsNothing().build()) {
    override val name = "colony"
    override val itemSettings = Item.Settings()
    override val blockEntityType = Companion.blockEntityType

    @Deprecated("Mojang is weird")
    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): Boolean {
        val upState = world.getBlockState(pos.up())

        if (isValidTargetBlock(upState)) {
            (world.getBlockEntity(pos) as? ColonyEntity)?.let { entity ->
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
            ItemStack(ModItems.bacteriumBunch, (0..3).random()).apply {
                getOrCreateTag().put(
                    "BacteriumData",
                    BacteriumBunchItem.Data(
                        // TODO: Bacterium data and evolution
                    ).toTag()
                )
            }
        )
    }

    companion object {
        val blockEntityType = BlockEntityType(::ColonyEntity, null)

        private fun isValidTargetBlock(state: BlockState) =
            !state.isAir && !state.block.matches(ModTags.inedibleTag)
    }

}
