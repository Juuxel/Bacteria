package juuxel.bacteria.lib

import juuxel.bacteria.blocks.ColonyBlock
import juuxel.bacteria.blocks.HumidifierBlock

object ModBlocks : ModRegistry() {
    lateinit var colony: ColonyBlock private set
    lateinit var humidifier: HumidifierBlock private set

    override fun init() {
        colony = registerBlock(ColonyBlock())
        humidifier = registerBlock(HumidifierBlock())
    }
}
