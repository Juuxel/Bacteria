package juuxel.bacteria.lib

import juuxel.bacteria.util.ModBlock
import juuxel.bacteria.util.ModContent
import net.minecraft.item.block.BlockItem
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

abstract class ModRegistry {
    abstract fun init()

    @Suppress("UNCHECKED_CAST")
    protected fun <R> register(registry: Registry<R>, content: ModContent<R>): ModContent<R> {
        Registry.register(
            registry,
            Identifier("bacteria", content.name),
            content.unwrap()
        )
        return content
    }

    protected fun <T : ModBlock> registerBlock(content: T): T {
        register(Registry.BLOCK, content)

        if (content.registerItem)
            Registry.register(
                Registry.ITEM,
                Identifier("bacteria", content.name),
                BlockItem(content.unwrap(), content.itemSettings)
            )
        if (content.blockEntityType != null)
            Registry.register(
                Registry.BLOCK_ENTITY,
                Identifier("bacteria", content.name),
                content.blockEntityType
            )

        return content
    }
}
