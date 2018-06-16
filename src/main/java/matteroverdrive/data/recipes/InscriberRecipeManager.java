/*
 * This file is part of Matter Overdrive
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */
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
