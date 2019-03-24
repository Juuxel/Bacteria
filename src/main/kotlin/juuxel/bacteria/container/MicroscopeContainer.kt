package juuxel.bacteria.container

import juuxel.bacteria.BacteriumData
import juuxel.bacteria.component.SimpleItemComponent
import juuxel.bacteria.item.BacteriumBunchItem
import juuxel.bacteria.item.ColonyItem
import net.minecraft.container.Slot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory

class MicroscopeContainer private constructor(syncId: Int, playerInv: PlayerInventory, inv: Inventory) :
    BacteriaContainer(syncId, inv, playerInv, listOf(Slot(inv, 0, 80, 35))) {
    constructor(syncId: Int, playerInv: PlayerInventory) : this(syncId, playerInv, MicroscopeInventory())

    override fun canUse(player: PlayerEntity?) = true

    private class MicroscopeInventory : SimpleItemComponent(1) {
        init {
            listen {
                val stack = this[0]

                if (!stack.isEmpty) {
                    when (stack.item) {
                        is BacteriumBunchItem ->
                            stack.tag?.let {
                                it.put(
                                    "BacteriumData",
                                    BacteriumData.fromTag(
                                        it.getCompound("BacteriumData")
                                    ).copy(isAnalyzed = true).toTag()
                                )
                            }

                        is ColonyItem ->
                            stack.tag?.getCompound("BlockEntityTag")?.let {
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
        }
    }
}
