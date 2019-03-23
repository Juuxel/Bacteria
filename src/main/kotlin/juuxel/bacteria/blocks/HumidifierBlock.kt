package juuxel.bacteria.blocks

import io.github.prospector.silk.util.ActionType
import juuxel.bacteria.component.SimpleItemComponent
import juuxel.bacteria.component.wrapper.SidedItemView
import juuxel.bacteria.container.HumidifierContainer
import juuxel.bacteria.lib.ModContainers
import juuxel.bacteria.lib.ModRecipes
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.container.ContainerProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.nbt.CompoundTag
import net.minecraft.recipe.RecipeFinder
import net.minecraft.recipe.RecipeInputProvider
import net.minecraft.util.Hand
import net.minecraft.util.Tickable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
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

    override fun getInventory(state: BlockState, world: IWorld, pos: BlockPos) =
        (world.getBlockEntity(pos) as? Entity)?.getInventory()

    class Entity private constructor(private val items: SimpleItemComponent) : BlockEntity(blockEntityType), ContainerProvider, RecipeInputProvider, Tickable {
        constructor() : this(SimpleItemComponent(1, 1))

        init {
            items.listen(this::markDirty)
        }

        override fun fromTag(tag: CompoundTag) {
            super.fromTag(tag)
            items.fromTag(tag.getTag("Items"))
        }

        override fun toTag(tag: CompoundTag) = super.toTag(tag).apply {
            put("Items", items.toTag())
        }

        override fun createMenu(syncId: Int, playerInv: PlayerInventory, player: PlayerEntity) =
            HumidifierContainer(syncId, items, playerInv)

        fun getInventory(): SidedInventory = SidedItemView(items)

        override fun provideRecipeInputs(finder: RecipeFinder) {
            finder.addItem(items[0])
        }

        override fun tick() {
            if (world.isClient) return

            if (!items[0].isEmpty) {
                val recipe = world.recipeManager[ModRecipes.humidifying, items, world]
                recipe.ifPresent {
                    if (world.random.nextInt(16) == 0) {
                        items[0].subtractAmount(1)
                        items.insert(it.craft(items), ActionType.PERFORM)
                    }
                }
            }
        }
    }

    companion object {
        val blockEntityType = BlockEntityType(::Entity, null)
    }
}
