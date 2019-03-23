package juuxel.bacteria.lib

import io.netty.buffer.Unpooled
import juuxel.bacteria.block.entity.HumidifierEntity
import juuxel.bacteria.container.gui.HumidifierScreen
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.packet.CustomPayloadS2CPacket
import net.minecraft.server.network.packet.CustomPayloadC2SPacket
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf
import net.minecraft.util.math.BlockPos

// TODO: Work out this mess
object ModPackets {
    val requestHumidifierProgress = Identifier("bacteria", "request_humidifier_progress")
    val humidifierProgress = Identifier("bacteria", "humidifier_progress")

    fun init() {
        ServerSidePacketRegistry.INSTANCE.register(requestHumidifierProgress) { context, buf ->
            val pos = buf.readBlockPos()
            val progress = (context.player.world.getBlockEntity(pos) as? HumidifierEntity)?.progress ?: return@register
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(context.player, createHumidifierProgress(progress))
        }
    }

    fun initClient() {
        ClientSidePacketRegistry.INSTANCE.register(humidifierProgress) { _, buf ->
            (MinecraftClient.getInstance().currentScreen as? HumidifierScreen)?.updateProgress(buf.readInt())
        }
    }

    fun createRequestHumidifierProgress(pos: BlockPos): CustomPayloadC2SPacket =
        CustomPayloadC2SPacket(requestHumidifierProgress, PacketByteBuf(Unpooled.buffer()).apply {
            writeBlockPos(pos)
        })

    fun createHumidifierProgress(progress: Int): CustomPayloadS2CPacket =
        CustomPayloadS2CPacket(humidifierProgress, PacketByteBuf(Unpooled.buffer()).apply {
            writeInt(progress)
        })
}