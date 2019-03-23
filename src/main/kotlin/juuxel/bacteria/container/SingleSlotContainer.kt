package juuxel.bacteria.container

import net.minecraft.container.Slot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory

class SingleSlotContainer(syncId: Int, inv: Inventory, playerInv: PlayerInventory) : BacteriaContainer(syncId, inv, playerInv) {
    init {
        this.addSlot(Slot(inv, 0, 80, 35))
    }

    override fun canUse(player: PlayerEntity?) = true
}
