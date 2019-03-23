package juuxel.bacteria

import io.github.cottonmc.cotton.config.ConfigManager
import juuxel.bacteria.lib.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.minecraft.world.GameRules

object Bacteria : ModInitializer {
    const val jamColoniesGameRule = "bacteria_jamColonies"
    lateinit var config: BacteriaConfig private set

    override fun onInitialize() {
        config = ConfigManager.loadConfig(BacteriaConfig::class.java)
        ModBlocks.init()
        ModCommands.init()
        ModItems.init()
        ModTags.init()
        ModContainers.init()
        ModRecipes.init()
        ModPackets.init()
        GameRules.getKeys()[jamColoniesGameRule] = GameRules.Key("false", GameRules.Type.BOOLEAN)
    }

    object Client : ClientModInitializer {
        override fun onInitializeClient() {
            ModContainers.initClient()
            ModPackets.initClient()
        }
    }
}
