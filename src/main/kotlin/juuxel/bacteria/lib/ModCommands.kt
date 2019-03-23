package juuxel.bacteria.lib

import juuxel.bacteria.Bacteria
import net.fabricmc.fabric.api.registry.CommandRegistry
import net.minecraft.server.command.ServerCommandManager
import net.minecraft.text.TranslatableTextComponent

object ModCommands {
    fun init() {
        CommandRegistry.INSTANCE.register(false) { dispatcher ->
            dispatcher.register(ServerCommandManager.literal("bacteria")
                .requires {
                    it.hasPermissionLevel(2)
                }
                .then(ServerCommandManager.literal("jam").executes {
                    it.source.world.gameRules[Bacteria.jamColoniesGameRule].set("true", it.source.minecraftServer)
                    it.source.sendFeedback(TranslatableTextComponent("chat.bacteria.jammed"), false)
                    1
                })
                .then(ServerCommandManager.literal("unjam").executes {
                    it.source.world.gameRules[Bacteria.jamColoniesGameRule].set("false", it.source.minecraftServer)
                    it.source.sendFeedback(TranslatableTextComponent("chat.bacteria.unjammed"), false)
                    1
                })
            )
        }
    }
}