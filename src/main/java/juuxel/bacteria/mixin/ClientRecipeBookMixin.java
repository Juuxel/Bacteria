package juuxel.bacteria.mixin;

import juuxel.bacteria.container.HumidifierContainer;
import net.minecraft.client.recipe.book.ClientRecipeBook;
import net.minecraft.client.recipe.book.RecipeBookGroup;
import net.minecraft.container.CraftingContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(method = "getGroupsForContainer", at = @At("HEAD"), cancellable = true)
    private static void onGetGroupsForContainer(CraftingContainer<?> container, CallbackInfoReturnable<List<RecipeBookGroup>> info) {
        if (container instanceof HumidifierContainer) {
            info.setReturnValue(Collections.singletonList(RecipeBookGroup.SEARCH));
        }
    }
}
