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

import matteroverdrive.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author shadowfacts
 */
public class InscriberRecipeCategory implements IRecipeCategory<InscriberRecipeWrapper> {

    public static final String UID = "mo.inscriber";
    private final IDrawable background;
    private final IDrawable overlay;
    private final String localizedName;

    public InscriberRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(120, 72);
        overlay = guiHelper.createDrawable(new ResourceLocation(Reference.PATH_GUI + "inscriber_jei.png"), 0, 0, 120, 72);
        localizedName = I18n.format("mo.jei.inscriber");
    }

    @Override
    public String getModName() {
        return Reference.MOD_NAME;
    }

    @Nonnull
    @Override
    public String getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        overlay.draw(minecraft);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, InscriberRecipeWrapper recipe, IIngredients ingredients) {
        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        recipeLayout.getItemStacks().init(0, true, 9, 14);
        recipeLayout.getItemStacks().set(0, inputs.get(0));
        recipeLayout.getItemStacks().init(1, true, 9, 41);
        recipeLayout.getItemStacks().set(1, inputs.get(1));

        recipeLayout.getItemStacks().init(2, false, 67, 14);

        recipeLayout.getItemStacks().set(2, ingredients.getOutputs(ItemStack.class).get(0));
    }
}
