package juuxel.bacteria.lib

import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.Block
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier

object ModTags {
    lateinit var inedibleTag: Tag<Block> private set

    fun init() {
        inedibleTag = TagRegistry.block(Identifier("bacteria", "inedible"))
    }
}
