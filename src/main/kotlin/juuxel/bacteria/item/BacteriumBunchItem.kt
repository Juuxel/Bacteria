package juuxel.bacteria.item

import juuxel.bacteria.util.ModContent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.FoodItemSetting
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.text.TextComponent
import net.minecraft.text.TextFormat
import net.minecraft.text.TranslatableTextComponent
import net.minecraft.util.DefaultedList
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World

class BacteriumBunchItem : Item(
    Settings().food(
        FoodItemSetting.Builder()
            .hunger(1)
            .alwaysEdible()
            .eatenFast()
            .build()
    ).itemGroup(ItemGroup.FOOD)
), ModContent<Item> {
    override val name = "bacterium_bunch"

    override fun onItemFinishedUsing(
        stack: ItemStack,
        world: World,
        entity: LivingEntity
    ): ItemStack = super.onItemFinishedUsing(stack, world, entity).apply {
        if (world.isClient) return@apply

        storeDataInStack(stack)
        stack.tag?.getCompound("BacteriumData")?.let(Data.Companion::fromTag)?.let {
            if (it.type.effects.isNotEmpty()) {
                entity.addPotionEffect(
                    StatusEffectInstance(
                        it.type.effects.random(),
                        600, 1
                    )
                )
            }
        }
    }

    private fun storeDataInStack(stack: ItemStack, data: Data = Data.default) {
        stack.getOrCreateTag().let { tag ->
            if (!tag.containsKey("BacteriumData")) {
                tag.put("BacteriumData", data.toTag())
            }
        }
    }

    override fun appendItemsForGroup(group: ItemGroup, list: DefaultedList<ItemStack>) {
        if (isInItemGroup(group)) {
            list += ItemStack(this).also { storeDataInStack(it, Data(type = Type.Harmful, isAnalyzed = true)) }
            list += ItemStack(this).also { storeDataInStack(it, Data(type = Type.Neutral, isAnalyzed = true)) }
            list += ItemStack(this).also { storeDataInStack(it, Data(type = Type.Helpful, isAnalyzed = true)) }
        }
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
            val data = it.getCompound("BacteriumData").let(Data.Companion::fromTag)

            list.add(
                TranslatableTextComponent(
                    "$translationKey.${data.type.translationKey}"
                ).applyFormat(TextFormat.DARK_GRAY).run {
                    if (!data.isAnalyzed)
                        applyFormat(TextFormat.OBFUSCATED)
                    else this
                }
            )
        }
    }

    data class Data(val lifetime: Double = 1.0, val hunger: Double = 1.0, val type: Type = Type.Harmful, val isAnalyzed: Boolean = false) {
        fun toTag(): CompoundTag =
            CompoundTag().apply {
                putDouble("Lifetime", lifetime)
                putDouble("Hunger", hunger)
                putInt("Type", this@Data.type.ordinal)
                putBoolean("Analyzed", isAnalyzed)
            }

        companion object {
            val default = Data()

            fun fromTag(tag: CompoundTag): Data {
                val lifetime =
                    if (tag.containsKey("Lifetime")) tag.getDouble("Lifetime")
                    else default.lifetime
                val hunger =
                    if (tag.containsKey("Hunger")) tag.getDouble("Hunger")
                    else default.hunger
                val type =
                    if (tag.containsKey("Lifetime"))
                        Type.values()[
                            MathHelper.clamp(tag.getInt("Type"), 0, Type.values().lastIndex)
                        ]
                    else default.type
                val isAnalyzed =
                    if (tag.containsKey("Analyzed")) tag.getBoolean("Analyzed")
                    else default.isAnalyzed

                return Data(lifetime, hunger, type, isAnalyzed)
            }
        }
    }

    enum class Type(val translationKey: String, vararg val effects: StatusEffect) {
        Helpful("helpful", StatusEffects.HASTE, StatusEffects.REGENERATION),
        Neutral("neutral"),
        Harmful("harmful", StatusEffects.WEAKNESS, StatusEffects.POISON, StatusEffects.NAUSEA, StatusEffects.HUNGER)
    }
}
