package juuxel.bacteria.container

import net.minecraft.container.Container
import net.minecraft.container.Slot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

abstract class BacteriaContainer(syncId: Int, private val inventory: Inventory, playerInv: PlayerInventory, slots: List<Slot>) : Container(null, syncId) {
    init {
        for (slot in slots)
            addSlot(slot)

        for (row in 0..2) {
            for (col in 0..8) {
                addSlot(Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18))
            }
        }

        for (i in 0..8) {
            addSlot(Slot(playerInv, i, 8 + i * 18, 142))
        }
    }

    override fun transferSlot(player: PlayerEntity?, slotIndex: Int): ItemStack {
        var stack = ItemStack.EMPTY
        val slot = this.slotList[slotIndex]
        if (slot != null && slot.hasStack()) {
            val slotStack = slot.stack
            stack = slotStack.copy()
            if (slotIndex < this.inventory.invSize) {
                if (!this.insertItem(slotStack, inventory.invSize, this.slotList.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.insertItem(slotStack, 0, inventory.invSize, false)) {
                return ItemStack.EMPTY
            }

            if (slotStack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }

        return stack
    }
}
