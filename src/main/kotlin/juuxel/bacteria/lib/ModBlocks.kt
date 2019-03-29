package juuxel.bacteria.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.bacteria.block.BacteriumComposterBlock
import juuxel.bacteria.block.ColonyBlock
import juuxel.bacteria.block.HumidifierBlock
import juuxel.bacteria.block.MicroscopeBlock

object ModBlocks : PolyesterRegistry("bacteria") {
    lateinit var colony: ColonyBlock private set
    lateinit var humidifier: HumidifierBlock private set
    lateinit var microscope: MicroscopeBlock private set
    lateinit var bacteriumComposter: BacteriumComposterBlock private set

    fun init() {
        colony = registerBlock(ColonyBlock())
        humidifier = registerBlock(HumidifierBlock())
        microscope = registerBlock(MicroscopeBlock())
        bacteriumComposter = registerBlock(BacteriumComposterBlock())
    }
}
