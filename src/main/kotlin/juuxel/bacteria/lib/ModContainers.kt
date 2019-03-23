package juuxel.bacteria.lib

import juuxel.bacteria.block.entity.HumidifierEntity
import juuxel.bacteria.component.SimpleItemComponent
import juuxel.bacteria.container.MicroscopeContainer
import juuxel.bacteria.container.gui.BacteriaContainerScreen
import juuxel.bacteria.container.gui.HumidifierScreen
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.util.Identifier

object ModContainers {
    val humidifier = Identifier("bacteria", "humidifier")
    val microscope = Identifier("bacteria", "microscope")

    fun init() {
        ContainerProviderRegistry.INSTANCE.registerFactory(humidifier) { syncId, id, player, buf ->
            val pos = buf.readBlockPos()
            val entity = player.world.getBlockEntity(pos)

            if (entity is HumidifierEntity)
                entity.createMenu(syncId, player.inventory, player)
            else
                null
        }

        ContainerProviderRegistry.INSTANCE.registerFactory(microscope) { syncId, id, player, buf ->
            MicroscopeContainer(syncId, player.inventory)
        }
    }

    fun initClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(humidifier) { syncId, id, player, buf ->
            val pos = buf.readBlockPos()
            val entity = player.world.getBlockEntity(pos)

            if (entity is HumidifierEntity)
                HumidifierScreen(entity.createMenu(syncId, player.inventory, player), player)
            else
                null
        }

        ScreenProviderRegistry.INSTANCE.registerFactory(microscope) { syncId, id, player, buf ->
            BacteriaContainerScreen(MicroscopeContainer(syncId, player.inventory), player, microscope.path)
        }
    }
}
