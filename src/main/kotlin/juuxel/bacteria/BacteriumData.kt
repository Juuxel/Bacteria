package juuxel.bacteria

import juuxel.bacteria.lib.ModItems
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.util.math.MathHelper

data class BacteriumData(val lifetime: Double = 1.0, val hunger: Double = 1.0, val type: Type = Type.Harmful, val isAnalyzed: Boolean = false) {
    fun toTag(): CompoundTag =
        CompoundTag().apply {
            putDouble("Lifetime", lifetime)
            putDouble("Hunger", hunger)
            putInt("Type", this@BacteriumData.type.ordinal)
            putBoolean("Analyzed", isAnalyzed)
        }

    fun toBunchItemStack(amount: Int = 1): ItemStack =
        ItemStack(ModItems.bacteriumBunch, amount).apply {
            getOrCreateTag().put("BacteriumData", toTag())
        }

    companion object {
        val default = BacteriumData()

        fun fromTag(tag: Tag): BacteriumData {
            require(tag is CompoundTag)

            val lifetime =
                if (tag.containsKey("Lifetime")) tag.getDouble("Lifetime")
                else default.lifetime
            val hunger =
                if (tag.containsKey("Hunger")) tag.getDouble("Hunger")
                else default.hunger
            val type =
                if (tag.containsKey("Lifetime"))
                    Type.values()[
                            MathHelper.clamp(
                                tag.getInt("Type"),
                                0,
                                Type.values().lastIndex
                            )
                    ]
                else default.type
            val isAnalyzed =
                if (tag.containsKey("Analyzed")) tag.getBoolean("Analyzed")
                else default.isAnalyzed

            return BacteriumData(lifetime, hunger, type, isAnalyzed)
        }
    }

    enum class Type(val translationKey: String, vararg val effects: StatusEffect) {
        Helpful("helpful",
            StatusEffects.HASTE,
            StatusEffects.REGENERATION
        ),
        Neutral("neutral"),
        Harmful("harmful",
            StatusEffects.WEAKNESS,
            StatusEffects.POISON,
            StatusEffects.NAUSEA,
            StatusEffects.HUNGER
        )
    }
}