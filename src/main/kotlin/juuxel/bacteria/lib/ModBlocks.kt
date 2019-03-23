package juuxel.bacteria.lib

import juuxel.bacteria.block.ColonyBlock
import juuxel.bacteria.block.HumidifierBlock
import juuxel.bacteria.block.MicroscopeBlock

object ModBlocks : ModRegistry() {
    lateinit var colony: ColonyBlock private set
    lateinit var humidifier: HumidifierBlock private set
    lateinit var microscope: MicroscopeBlock private set

    override fun init() {
        colony = registerBlock(ColonyBlock())
        humidifier = registerBlock(HumidifierBlock())
        microscope = registerBlock(MicroscopeBlock())
    }
}
