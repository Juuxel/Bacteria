package juuxel.bacteria.util

import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.Item

interface ModContent<out T> {
    val name: String

    @Suppress("UNCHECKED_CAST")
    fun unwrap(): T = this as T
}

interface BlockLikeContent<out T> : ModContent<T> {
    val hasDescription: Boolean get() = false
    val descriptionKey: String get() = "%TranslationKey.desc"
    val itemSettings: Item.Settings?
}

interface ModBlock : BlockLikeContent<Block> {
    val blockEntityType: BlockEntityType<*>? get() = null
}
