package juuxel.bacteria.container

import juuxel.bacteria.BacteriumData
import juuxel.bacteria.component.SimpleItemComponent
import juuxel.bacteria.item.BacteriumBunchItem
import net.minecraft.container.Slot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class MicroscopeContainer private constructor(syncId: Int, playerInv: PlayerInventory, inv: Inventory) :
    BacteriaContainer(syncId, inv, playerInv, listOf(MicroscopeSlot(inv, 0, 80, 35))) {
    constructor(syncId: Int, playerInv: PlayerInventory) : this(syncId, playerInv, MicroscopeInventory())

    override fun canUse(player: PlayerEntity?) = true

    private class MicroscopeInventory : SimpleItemComponent(1) {
        init {
            listen {
                val stack = this[0]

                if (!stack.isEmpty && stack.item is BacteriumBunchItem) { // double-checking just to be sure
                    stack.tag?.let {
                        it.put(
                            "BacteriumData",
                            BacteriumData.fromTag(
                                it.getCompound("BacteriumData")
                            ).copy(isAnalyzed = true).toTag()
                        )
                    }
                }
            }
        }

//        override fun getInvMaxStackAmount() = 1
    }

    private class MicroscopeSlot(inv: Inventory, i: Int, x: Int, y: Int) : Slot(inv, i, x, y) {
        override fun canInsert(stack: ItemStack) = stack.item is BacteriumBunchItem
//        override fun getMaxStackAmount() = 1
    }
}
