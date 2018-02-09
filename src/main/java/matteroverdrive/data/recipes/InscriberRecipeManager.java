package matteroverdrive.data.recipes;

import matteroverdrive.tile.TileEntityInscriber;
import net.minecraft.item.ItemStack;

/**
 * @author shadowfacts
 */
public class InscriberRecipeManager extends RecipeManager<TileEntityInscriber, InscriberRecipe> {

    public InscriberRecipeManager() {
        super(InscriberRecipe.class);
    }

    public boolean isPrimaryInput(ItemStack stack) {
        return recipes.stream()
                .map(InscriberRecipe::getMain)
                .anyMatch(s -> ItemStack.areItemsEqual(s, stack));
    }

    public boolean isSecondaryInput(ItemStack stack) {
        return recipes.stream()
                .map(InscriberRecipe::getSec)
                .anyMatch(s -> ItemStack.areItemsEqual(s, stack));
    }

}
