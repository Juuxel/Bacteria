package juuxel.bacteria.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.bacteria.item.BacteriumBunchItem
import juuxel.bacteria.item.ColonyItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

object ModItems : PolyesterRegistry("bacteria") {
    lateinit var bacteriumBunch: Item private set

    fun init() {
        bacteriumBunch = register(Registry.ITEM, BacteriumBunchItem()).unwrap()
        register(Registry.ITEM, ColonyItem())
    }
}
