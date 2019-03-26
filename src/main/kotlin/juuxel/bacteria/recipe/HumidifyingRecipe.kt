package juuxel.bacteria.recipe

import com.google.gson.JsonObject
import juuxel.bacteria.BacteriumData
import juuxel.bacteria.lib.ModItems
import juuxel.bacteria.lib.ModRecipes
import juuxel.bacteria.util.ModContent
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.PacketByteBuf
import net.minecraft.world.World

class HumidifyingRecipe(private val input: Ingredient) : Recipe<Inventory>, ModContent<Recipe<Inventory>> {
    override val name = "humidifying"
    private val recipeId = Identifier("bacteria", name)
    private val output = BacteriumData.default.toBunchItemStack()

    override fun craft(inv: Inventory) = ItemStack(ModItems.bacteriumBunch).apply {
        getOrCreateTag().put("BacteriumData", BacteriumData(type = getCompatibleType(inv.getInvStack(1))).toTag())
    }
    override fun getId() = recipeId
    override fun getType() = ModRecipes.humidifying
    override fun fits(p0: Int, p1: Int) = true
    override fun getSerializer() = ModRecipes.humidifyingSerializer
    override fun getOutput(): ItemStack = output
    override fun matches(inv: Inventory, world: World?) = input.test(inv.getInvStack(0))

    private fun getCompatibleType(stack: ItemStack): BacteriumData.Type =
        if (stack.isEmpty || stack.tag == null) BacteriumData.Type.values().random()
        else BacteriumData.Type.fromInt(stack.tag!!.getCompound("BacteriumData").getInt("Type"))

    class Serializer : RecipeSerializer<HumidifyingRecipe> {
        override fun write(buf: PacketByteBuf, recipe: HumidifyingRecipe) {
            recipe.input.write(buf)
        }

        override fun read(id: Identifier, obj: JsonObject): HumidifyingRecipe {
            val ingredientInput =
                if (JsonHelper.hasArray(obj, "ingredient"))
                    JsonHelper.getArray(obj, "ingredient")
                else
                    JsonHelper.getObject(obj, "ingredient")

            return HumidifyingRecipe(Ingredient.fromJson(ingredientInput))
        }

        override fun read(id: Identifier, buf: PacketByteBuf) =
            HumidifyingRecipe(Ingredient.fromPacket(buf))
    }
}