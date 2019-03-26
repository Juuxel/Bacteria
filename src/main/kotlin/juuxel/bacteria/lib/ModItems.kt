package juuxel.bacteria.lib

import juuxel.bacteria.item.BacteriumBunchItem
import juuxel.bacteria.item.ColonyItem
import juuxel.bacteria.item.TesterItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

object ModItems : ModRegistry() {
    lateinit var bacteriumBunch: Item private set

    override fun init() {
        bacteriumBunch = register(Registry.ITEM, BacteriumBunchItem()).unwrap()
        register(Registry.ITEM, ColonyItem())
        register(Registry.ITEM, TesterItem())
    }
}
