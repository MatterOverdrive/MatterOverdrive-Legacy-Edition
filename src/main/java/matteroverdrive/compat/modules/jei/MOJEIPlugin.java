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
package matteroverdrive.compat.modules.jei;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.container.ContainerInscriber;
import matteroverdrive.data.recipes.InscriberRecipe;
import matteroverdrive.gui.GuiInscriber;
import matteroverdrive.init.MatterOverdriveRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author shadowfacts
 */
@JEIPlugin
public class MOJEIPlugin implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new InscriberRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {

    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.handleRecipes(InscriberRecipe.class, new InscriberRecipeHandler(), InscriberRecipeCategory.UID);

        registry.addRecipes(MatterOverdriveRecipes.INSCRIBER.getRecipes(), InscriberRecipeCategory.UID);

        registry.addRecipeCatalyst(new ItemStack(MatterOverdrive.BLOCKS.inscriber), InscriberRecipeCategory.UID);

        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerInscriber.class, InscriberRecipeCategory.UID, 0, 2, 8, 36);

        registry.addRecipeClickArea(GuiInscriber.class, 32, 55, 24, 16, InscriberRecipeCategory.UID);

        registry.addAdvancedGuiHandlers(new MOAdvancedGuiHandler());

        registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(MatterOverdrive.BLOCKS.boundingBox));
    }

}