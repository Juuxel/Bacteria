package juuxel.bacteria.blocks

import juuxel.bacteria.container.SingleSlotContainer
import juuxel.bacteria.lib.ModContainers
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LockableContainerBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.BasicInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.text.TranslatableTextComponent
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HumidifierBlock : BBlockWithEntity(FabricBlockSettings.copy(Blocks.BLAST_FURNACE).lightLevel(0).build()) {
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

    class Entity private constructor(private val inventory: Inventory) : LockableContainerBlockEntity(blockEntityType), Inventory by inventory {
        constructor() : this(BasicInventory(1))

        override fun markDirty() {
            super.markDirty()
            inventory.markDirty()
        }

        // TODO: Serialize the inventory
        // Using TTBasicInventory?

        override fun createContainer(syncId: Int, playerInv: PlayerInventory) = SingleSlotContainer(syncId, this, playerInv)
        override fun getContainerName() = TranslatableTextComponent("container.bacteria.humidifier")
    }

    companion object {
        val blockEntityType = BlockEntityType(::Entity, null)
    }
}
