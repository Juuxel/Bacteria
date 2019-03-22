package juuxel.bacteria

import juuxel.bacteria.blocks.ColonyBlock
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.registry.CommandRegistry
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.Item
import net.minecraft.item.block.BlockItem
import net.minecraft.server.command.ServerCommandManager
import net.minecraft.tag.Tag
import net.minecraft.text.TranslatableTextComponent
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.GameRules

object Bacteria : ModInitializer {
    lateinit var colonyBlock: ColonyBlock private set
    lateinit var colonyBEType: BlockEntityType<ColonyBlock.Entity> private set
    lateinit var inedibleTag: Tag<Block> private set
    const val jamColoniesGameRule = "bacteria_jamColonies"

    override fun onInitialize() {
        colonyBlock = Registry.register(Registry.BLOCK, Identifier("bacteria", "colony"), ColonyBlock())
        colonyBEType = Registry.register(
            Registry.BLOCK_ENTITY,
            Identifier("bacteria", "colony"),
            BlockEntityType({ ColonyBlock.Entity() }, null)
        )
        Registry.register(Registry.ITEM, Identifier("bacteria", "colony"), BlockItem(colonyBlock, Item.Settings()))
        GameRules.getKeys()[jamColoniesGameRule] = GameRules.Key("false", GameRules.Type.BOOLEAN)
        inedibleTag = TagRegistry.block(Identifier("bacteria", "inedible"))

        CommandRegistry.INSTANCE.register(false) { dispatcher ->
            dispatcher.register(ServerCommandManager.literal("bacteria")
                .requires {
                    it.hasPermissionLevel(2)
                }
                .then(ServerCommandManager.literal("jam").executes {
                    it.source.world.gameRules[jamColoniesGameRule].set("true", it.source.minecraftServer)
                    it.source.sendFeedback(TranslatableTextComponent("chat.bacteria.jammed"), false)
                    1
                })
                .then(ServerCommandManager.literal("unjam").executes {
                    it.source.world.gameRules[jamColoniesGameRule].set("false", it.source.minecraftServer)
                    it.source.sendFeedback(TranslatableTextComponent("chat.bacteria.unjammed"), false)
                    1
                })
            )
        }
    }
}
