package juuxel.bacteria.lib

import juuxel.bacteria.recipe.HumidifyingRecipe
import net.minecraft.util.registry.Registry

object ModRecipes : ModRegistry() {
    val humidifying = registerRecipe<HumidifyingRecipe>("humidifying")
    val humidifyingSerializer = register(Registry.RECIPE_SERIALIZER, "humidifying", HumidifyingRecipe.Serializer())

    override fun init() {}
}