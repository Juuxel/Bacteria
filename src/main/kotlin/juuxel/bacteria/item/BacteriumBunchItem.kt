package juuxel.bacteria.item

import juuxel.bacteria.Bacteria
import juuxel.bacteria.BacteriumData
import juuxel.bacteria.block.BacteriumComposterBlock
import juuxel.bacteria.block.entity.BacteriumComposterEntity
import juuxel.bacteria.lib.ModBlocks
import juuxel.bacteria.util.ModContent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Blocks
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.*
import net.minecraft.state.property.Properties
import net.minecraft.text.TextComponent
import net.minecraft.text.TextFormat
import net.minecraft.text.TranslatableTextComponent
import net.minecraft.util.ActionResult
import net.minecraft.util.DefaultedList
import net.minecraft.world.World
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class BacteriumBunchItem : Item(
    Settings().food(
        FoodItemSetting.Builder()
            .hunger(1)
            .alwaysEdible()
            .eatenFast()
            .build()
    ).itemGroup(Bacteria.itemGroup)
), ModContent<Item> {
    override val name = "bacterium_bunch"

    override fun onItemFinishedUsing(
        stack: ItemStack,
        world: World,
        entity: LivingEntity
    ): ItemStack = super.onItemFinishedUsing(stack, world, entity).apply {
        if (world.isClient) return@apply

        storeDataInStack(stack)
        stack.tag?.getCompound("BacteriumData")?.let(BacteriumData.Companion::fromTag)?.let {
            if (it.type.effects.isNotEmpty()) {
                entity.addPotionEffect(
                    StatusEffectInstance(
                        it.type.effects.random(),
                        600, 1
                    )
                )
            }

            val hungerLevel = (it.hunger - 2.0).roundToInt()
            if (hungerLevel >= 0) {
                entity.addPotionEffect(
                    StatusEffectInstance(
                        StatusEffects.HUNGER,
                        600, hungerLevel
                    )
                )
            }
        }
    }

    private fun storeDataInStack(stack: ItemStack, data: BacteriumData = BacteriumData.default) {
        stack.getOrCreateTag().let { tag ->
            if (!tag.containsKey("BacteriumData")) {
                tag.put("BacteriumData", data.toTag())
            }
        }
    }

    override fun appendItemsForGroup(group: ItemGroup, list: DefaultedList<ItemStack>) {
        if (isInItemGroup(group)) {
            list += ItemStack(this).also { storeDataInStack(it,
                BacteriumData(type = BacteriumData.Type.Harmful, isAnalyzed = true)
            ) }
            list += ItemStack(this).also { storeDataInStack(it,
                BacteriumData(type = BacteriumData.Type.Neutral, isAnalyzed = true)
            ) }
            list += ItemStack(this).also { storeDataInStack(it,
                BacteriumData(type = BacteriumData.Type.Helpful, isAnalyzed = true)
            ) }
        }
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        if (context.world.isClient) return super.useOnBlock(context)

        val world = context.world
        val stack = context.itemStack
        val pos = context.blockPos
        val state = context.world.getBlockState(pos)
        var filled = false
        var level = 0

        if (state.block == Blocks.COMPOSTER) {
            if (state[Properties.COMPOSTER_LEVEL] == 0) {
                // Replacing the composter with a bacterium composter
                world.setBlockState(pos, ModBlocks.bacteriumComposter.defaultState)
                filled = true
            }
        }

        if (state.block == ModBlocks.bacteriumComposter) {
            level = state[BacteriumComposterBlock.LEVEL]

            if (level < 4) {
                filled = true
            }
        }

        if (filled) {
            val entity = world.getBlockEntity(pos) as? BacteriumComposterEntity ?: return super.useOnBlock(context)

            entity.contents.add(
                BacteriumData.fromTag(
                    stack.tag?.getCompound("BacteriumData") ?: return super.useOnBlock(context)
                )
            )

            if (context.player?.isCreative != true) {
                stack.subtractAmount(1)
            }

            world.setBlockState(
                pos,
                ModBlocks.bacteriumComposter.defaultState.with(BacteriumComposterBlock.LEVEL, level + 1)
            )

            return ActionResult.SUCCESS
        }

        return super.useOnBlock(context)
    }

    @Environment(EnvType.CLIENT)
    override fun buildTooltip(
        stack: ItemStack,
        world: World?,
        list: MutableList<TextComponent>,
        context: TooltipContext?
    ) {
        storeDataInStack(stack)
        stack.tag?.let {
            val data = it.getCompound("BacteriumData").let(BacteriumData.Companion::fromTag)

            list.add(
                TranslatableTextComponent(
                    "$translationKey.${data.type.translationKey}"
                ).applyFormat(TextFormat.DARK_GRAY).run {
                    if (!data.isAnalyzed)
                        applyFormat(TextFormat.OBFUSCATED)
                    else this
                }
            )

            if (data.isAnalyzed) {
                list.add(
                    TranslatableTextComponent("$translationKey.lifetime", numberFormat.format(data.lifetime))
                        .applyFormat(TextFormat.DARK_GRAY)
                )
                list.add(
                    TranslatableTextComponent("$translationKey.hunger", numberFormat.format(data.hunger))
                        .applyFormat(TextFormat.DARK_GRAY)
                )
            }
        }
    }

    companion object {
        internal val numberFormat = (NumberFormat.getNumberInstance(Locale.ROOT).clone() as NumberFormat).apply {
            minimumFractionDigits = 1
            maximumFractionDigits = 2
        }
    }
}
