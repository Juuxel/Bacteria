package juuxel.bacteria.block.entity

import io.github.prospector.silk.util.ActionType
import juuxel.bacteria.block.HumidifierBlock
import juuxel.bacteria.component.SimpleItemComponent
import juuxel.bacteria.component.wrapper.SidedItemView
import juuxel.bacteria.container.HumidifierContainer
import juuxel.bacteria.lib.ModRecipes
import net.minecraft.block.entity.BlockEntity
import net.minecraft.container.ContainerProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.nbt.CompoundTag
import net.minecraft.recipe.RecipeFinder
import net.minecraft.recipe.RecipeInputProvider
import net.minecraft.util.Tickable

class HumidifierEntity : BlockEntity(HumidifierBlock.blockEntityType), ContainerProvider, RecipeInputProvider, Tickable {
    private val items = SimpleItemComponent(1, 1)

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