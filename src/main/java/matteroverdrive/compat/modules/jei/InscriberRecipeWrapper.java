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

import com.google.common.collect.ImmutableList;
import matteroverdrive.Reference;
import matteroverdrive.data.recipes.InscriberRecipe;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.RenderUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @author shadowfacts
 */
public class InscriberRecipeWrapper implements IRecipeWrapper {

    private static final ResourceLocation ARROW = new ResourceLocation(Reference.TEXTURE_ARROW_PROGRESS);

    private final List<ItemStack> inputs;
    private final List<ItemStack> outputs;
    private final int energy;
    private final int time;

    public InscriberRecipeWrapper(InscriberRecipe recipe) {
        inputs = ImmutableList.of(recipe.getMain(), recipe.getSec());
        outputs = ImmutableList.of(recipe.getOutput());
        energy = recipe.getEnergy();
        time = recipe.getTime();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, outputs);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(ARROW);
        int width = (int) (((float) (minecraft.world.getTotalWorldTime() % time) / (float) time) * 24);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        RenderUtils.drawPlaneWithUV(35, 15, 0, width, 16, 0.5, 0, width / 48f, 1);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();

        String line1 = String.format("-%,d%s", energy, MOEnergyHelper.ENERGY_UNIT);
        int line1W = minecraft.fontRenderer.getStringWidth(line1);
        minecraft.fontRenderer.drawStringWithShadow(line1, 44 + 69 / 2 - (line1W) / 2, 38, Reference.COLOR_HOLO_RED.getColor());
        String line2 = I18n.format("mo.jei.time", time / 20);
        int line2W = minecraft.fontRenderer.getStringWidth(line2);
        minecraft.fontRenderer.drawStringWithShadow(line2, 44 + 69 / 2 - (line2W) / 2, 53, Reference.COLOR_HOLO_RED.getColor());
    }

}
