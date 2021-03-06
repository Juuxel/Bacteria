package juuxel.bacteria.block.entity

import io.github.juuxel.polyester.container.property.CompoundPropertyDelegate
import io.github.juuxel.polyester.container.property.SimplePropertyDelegate
import io.github.prospector.silk.fluid.DropletValues
import io.github.prospector.silk.util.ActionType
import juuxel.bacteria.block.HumidifierBlock
import juuxel.bacteria.component.SimpleFluidComponent
import juuxel.bacteria.component.SimpleItemComponent
import juuxel.bacteria.component.wrapper.SidedItemView
import juuxel.bacteria.container.HumidifierContainer
import juuxel.bacteria.lib.ModRecipes
import net.minecraft.block.entity.BlockEntity
import net.minecraft.container.ContainerProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.SidedInventory
import net.minecraft.nbt.CompoundTag
import net.minecraft.recipe.RecipeFinder
import net.minecraft.recipe.RecipeInputProvider
import net.minecraft.util.Tickable
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper

class HumidifierEntity : BlockEntity(HumidifierBlock.blockEntityType), ContainerProvider, RecipeInputProvider, Tickable {
    private val items = SimpleItemComponent(2)
    val tank = SimpleFluidComponent(1, TANK_SIZE)
    var progress = 0
        private set
    val propertyDelegate = CompoundPropertyDelegate(
        SimplePropertyDelegate(this::progress, { progress = it }),
        SimplePropertyDelegate({ tank[0].amount }, { tank[0].amount = it })
    )

    init {
        items.listen(this::markDirty)
        tank.listen(this::markDirty)
    }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        items.fromTag(tag.getTag("Items"))
        progress = MathHelper.clamp(tag.getInt("Progress"), 0, MAX_PROGRESS)
        tank.fromTag(tag.getTag("Tank"))
    }

    override fun toTag(tag: CompoundTag) = super.toTag(tag).apply {
        put("Items", items.toTag())
        putInt("Progress", progress)
        put("Tank", tank.toTag())
    }

    override fun createMenu(syncId: Int, playerInv: PlayerInventory, player: PlayerEntity) =
        HumidifierContainer(syncId, items, playerInv, propertyDelegate)

    fun getInventory(): SidedInventory = SidedItemView(items)

    override fun provideRecipeInputs(finder: RecipeFinder) {
        finder.addItem(items[0])
    }

    override fun tick() {
        if (world.isClient) return
        val initialProgress = progress

        if (!items[0].isEmpty) {
            val optionalRecipe = world.recipeManager[ModRecipes.humidifying, items, world]
            if (optionalRecipe.isPresent) {
                val recipe = optionalRecipe.get()

                if (progress < MAX_PROGRESS) {
                    progress++

                    if (tank.canExtractFluid(Direction.UP, Fluids.WATER, 1)) {
                        tank.extract(0, 1, ActionType.PERFORM)
                        progress++
                    }
                }

                if (progress >= MAX_PROGRESS) {
                    if (items[1].amount < 64) { // a not very fancy space check, since we know the output
                        items[0].subtractAmount(1)
                        items.insert(1, recipe.craft(items), ActionType.PERFORM)
                        progress = 0
                    }
                }
            } else {
                progress = 0
            }
        } else {
            progress = 0
        }

        if (progress != initialProgress) {
            markDirty()
        }
    }

    companion object {
        const val MAX_PROGRESS = 2400
        const val TANK_SIZE = 4 * DropletValues.BUCKET
    }
}
