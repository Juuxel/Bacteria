package juuxel.bacteria.block.entity

import juuxel.bacteria.BacteriumData
import juuxel.bacteria.block.BacteriumComposterBlock
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag

class BacteriumComposterEntity : BlockEntity(BacteriumComposterBlock.blockEntityType) {
    internal var contents: MutableList<BacteriumData> = ArrayList(4)
        private set

    override fun toTag(tag: CompoundTag) =
        super.toTag(tag).apply {
            put("Contents", ListTag().apply {
                contents.forEachIndexed { i, data ->
                    addTag(i, data.toTag())
                }
            })
        }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        contents = tag.getList("Contents", NbtType.COMPOUND).map(BacteriumData.Companion::fromTag).toMutableList()
    }
}