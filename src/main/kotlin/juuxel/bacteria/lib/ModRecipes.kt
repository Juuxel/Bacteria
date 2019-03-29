package juuxel.bacteria.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.bacteria.recipe.HumidifyingRecipe
import net.minecraft.util.registry.Registry

object ModRecipes : PolyesterRegistry("bacteria") {
    val humidifying = registerRecipe<HumidifyingRecipe>("humidifying")
    val humidifyingSerializer = register(Registry.RECIPE_SERIALIZER, "humidifying", HumidifyingRecipe.Serializer())

    fun init() {}
}