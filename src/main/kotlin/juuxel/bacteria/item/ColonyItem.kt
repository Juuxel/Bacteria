package juuxel.bacteria.item

import juuxel.bacteria.Bacteria
import juuxel.bacteria.BacteriumData
import juuxel.bacteria.lib.ModBlocks
import juuxel.bacteria.lib.ModItems
import juuxel.bacteria.util.ModContent
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.block.BlockItem
import net.minecraft.text.TextComponent
import net.minecraft.text.TextFormat
import net.minecraft.text.TranslatableTextComponent
import net.minecraft.util.DefaultedList
import net.minecraft.world.World

class ColonyItem : BlockItem(ModBlocks.colony, Settings().itemGroup(Bacteria.itemGroup)), ModContent<BlockItem> {
    override val name = "colony"

    override fun appendItemsForGroup(group: ItemGroup, list: DefaultedList<ItemStack>) {
        if (isInItemGroup(group)) {
            list.add(ItemStack(this).apply {
                getOrCreateSubCompoundTag("BlockEntityTag")
                    .put("BacteriumData", BacteriumData(isAnalyzed = true).toTag())
            })
        }
    }

    override fun buildTooltip(
        stack: ItemStack, world: World?, list: MutableList<TextComponent>, context: TooltipContext?
    ) {
        super.buildTooltip(stack, world, list, context)

        if (stack.hasTag() && stack.tag!!.getCompound("BlockEntityTag").containsKey("BacteriumData")) {
            val data = BacteriumData.fromTag(stack.tag!!.getCompound("BlockEntityTag").getCompound("BacteriumData"))

            if (data.isAnalyzed) {
                val start = ModItems.bacteriumBunch.translationKey

                list.add(
                    TranslatableTextComponent("$start.lifetime", BacteriumBunchItem.numberFormat.format(data.lifetime))
                        .applyFormat(TextFormat.DARK_GRAY)
                )
                list.add(
                    TranslatableTextComponent("$start.hunger", BacteriumBunchItem.numberFormat.format(data.hunger))
                        .applyFormat(TextFormat.DARK_GRAY)
                )
            }
        }
    }
}
