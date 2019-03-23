package juuxel.bacteria.lib

import juuxel.bacteria.item.BacteriumBunchItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

object ModItems : ModRegistry() {
    lateinit var bacteriumBunch: Item private set

    override fun init() {
        bacteriumBunch = register(Registry.ITEM, BacteriumBunchItem()).unwrap()
    }
}
