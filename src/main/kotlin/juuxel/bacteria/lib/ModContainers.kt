package juuxel.bacteria.lib

import juuxel.bacteria.block.entity.HumidifierEntity
import juuxel.bacteria.container.HumidifierContainer
import juuxel.bacteria.container.gui.HumidifierScreen
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
                HumidifierContainer(syncId, inventory, player.inventory)
            else
                null
        }
    }

    fun initClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(humidifier) { syncId, id, player, buf ->
            val pos = buf.readBlockPos()
            val entity = player.world.getBlockEntity(pos)

            if (entity is HumidifierEntity)
                HumidifierScreen(syncId, entity, player)
            else
                null
        }
    }
}
