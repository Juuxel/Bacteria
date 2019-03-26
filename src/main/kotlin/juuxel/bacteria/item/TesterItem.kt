package juuxel.bacteria.item

import juuxel.bacteria.block.entity.HumidifierEntity
import juuxel.bacteria.util.ModContent
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.text.StringTextComponent
import net.minecraft.util.ActionResult

class TesterItem : Item(Settings()), ModContent<Item> {
    override val name = "tester"

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val entity = context.world.getBlockEntity(context.blockPos)

        when (entity) {
            is HumidifierEntity -> {
                context.player?.addChatMessage(StringTextComponent("world.isClient: " + context.world.isClient), false)
                context.player?.addChatMessage(StringTextComponent("propertyDelegate[0]: " + entity.propertyDelegate[0]), false)
                context.player?.addChatMessage(StringTextComponent("progress: " + entity.progress), false)

                return ActionResult.SUCCESS
            }
        }

        return super.useOnBlock(context)
    }
}
