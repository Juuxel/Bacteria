package juuxel.bacteria.container

import net.minecraft.container.CraftingContainer
import net.minecraft.container.Slot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeFinder
import net.minecraft.recipe.RecipeInputProvider
import net.minecraft.util.math.BlockPos

class HumidifierContainer(
    syncId: Int, private val inv: Inventory, playerInv: PlayerInventory, val pos: BlockPos
) : CraftingContainer<Inventory>(null, syncId) {
    private val world = playerInv.player.world

    init {
        // The input slot
        addSlot(Slot(inv, 0, 60, 35))
        // The output slot
        addSlot(object : Slot(inv, 1, 110, 35) {
            override fun canInsert(stack: ItemStack) = false
        })

        // Player inventory
        for (row in 0..2) {
            for (col in 0..8) {
                addSlot(Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18))
            }
        }

        for (i in 0..8) {
            addSlot(Slot(playerInv, i, 8 + i * 18, 142))
        }
    }


    override fun populateRecipeFinder(finder: RecipeFinder) {
        if (inv is RecipeInputProvider)
            inv.provideRecipeInputs(finder)
    }

    override fun canUse(player: PlayerEntity?) = true
    override fun getCraftingResultSlotIndex() = 0
    override fun getCraftingWidth() = 1
    override fun getCraftingHeight() = 1
    override fun getCraftingSlotCount() = 1
    override fun matches(recipe: Recipe<in Inventory>) = recipe.matches(inv, world)
    override fun clearCraftingSlots() = inv.clear()

    override fun transferSlot(playerEntity_1: PlayerEntity?, int_1: Int): ItemStack {
        var stack1 = ItemStack.EMPTY
        val slot = slotList[int_1]
        if (slot != null && slot.hasStack()) {
            val stack2 = slot.stack
            stack1 = stack2.copy()
            if (int_1 < inv.invSize) {
                if (!insertItem(stack2, inv.invSize, slotList.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!insertItem(stack2, 0, inv.invSize, false)) {
                return ItemStack.EMPTY
            }

            if (stack2.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }

        return stack1
    }
}