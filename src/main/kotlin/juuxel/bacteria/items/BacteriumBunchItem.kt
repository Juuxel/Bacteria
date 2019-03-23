package juuxel.bacteria.items

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
import net.minecraft.text.StringTextComponent
import net.minecraft.text.TextComponent
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
        stack.tag?.getCompound("BacteriumData")?.let(Data.Companion::fromNbt)?.let {
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
                tag.put("BacteriumData", data.toNbt())
            }
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
        list.add(
            StringTextComponent(
                stack.tag?.getCompound("BacteriumData")
                    ?.let(Data.Companion::fromNbt)?.type?.name ?: ""
            )
        )
    }

    data class Data(val lifetime: Double = 1.0, val hunger: Double = 1.0, val type: Type = Type.Harmful) {
        fun toNbt(): CompoundTag =
            CompoundTag().apply {
                putDouble("Lifetime", lifetime)
                putDouble("Hunger", hunger)
                putInt("Type", this@Data.type.ordinal)
            }

        companion object {
            val default = Data()

            fun fromNbt(tag: CompoundTag): Data {
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

                return Data(lifetime, hunger, type)
            }
        }
    }

    enum class Type(vararg val effects: StatusEffect) {
        Helpful(StatusEffects.HASTE, StatusEffects.REGENERATION),
        Neutral,
        Harmful(StatusEffects.WEAKNESS, StatusEffects.POISON, StatusEffects.NAUSEA, StatusEffects.HUNGER)
    }
}
