package juuxel.bacteria.lib

import juuxel.bacteria.container.SingleSlotContainer
import juuxel.bacteria.container.gui.SingleSlotContainerScreen
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.entity.HopperBlockEntity
import net.minecraft.util.Identifier

object ModContainers {
    val humidifier = Identifier("bacteria", "humidifier")

    fun init() {
        ContainerProviderRegistry.INSTANCE.registerFactory(humidifier) { syncId, id, player, buf ->
            val pos = buf.readBlockPos()
            val inventory = HopperBlockEntity.getInventoryAt(player.world, pos)

            if (inventory != null)
                SingleSlotContainer(syncId, inventory, player.inventory)
            else
                null
        }
    }

    fun initClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(humidifier) { syncId, id, player, buf ->
            val pos = buf.readBlockPos()
            val inventory = HopperBlockEntity.getInventoryAt(player.world, pos)

            if (inventory != null)
                SingleSlotContainerScreen(syncId, inventory, player, humidifier.path)
            else
                null
        }
    }
}
