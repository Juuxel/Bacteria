package juuxel.bacteria

import io.github.cottonmc.cotton.config.ConfigManager
import juuxel.bacteria.lib.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.minecraft.client.render.block.BiomeColors
import net.minecraft.client.render.block.BlockColorMapper
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.world.GameRules

object Bacteria : ModInitializer {
    const val jamColoniesGameRule = "bacteria_jamColonies"
    lateinit var config: BacteriaConfig private set
    val itemGroup = FabricItemGroupBuilder.build(Identifier("bacteria", "items")) { ItemStack(ModItems.bacteriumBunch) }

    override fun onInitialize() {
        config = ConfigManager.loadConfig(BacteriaConfig::class.java)
        ModBlocks.init()
        ModCommands.init()
        ModItems.init()
        ModTags.init()
        ModContainers.init()
        ModRecipes.init()
        GameRules.getKeys()[jamColoniesGameRule] = GameRules.Key("false", GameRules.Type.BOOLEAN)
    }

    object Client : ClientModInitializer {
        override fun onInitializeClient() {
            ModContainers.initClient()
            ColorProviderRegistry.BLOCK.register(BlockColorMapper { _, view, pos, _ ->
                if (view != null && pos != null)
                    BiomeColors.waterColorAt(view, pos)
                else -1
            }, ModBlocks.humidifier)
        }
    }
}
