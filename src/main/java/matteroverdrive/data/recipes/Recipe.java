package matteroverdrive.data.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

/**
 * @author shadowfacts
 */
public abstract class Recipe<M> {

    public abstract boolean matches(M machine);

    public abstract List<Ingredient> getInputs();

    public abstract ItemStack getOutput(M machine);
}
