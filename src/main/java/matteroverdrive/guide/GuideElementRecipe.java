/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
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

package matteroverdrive.guide;

import matteroverdrive.Reference;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.w3c.dom.Element;

/**
 * Created by Simeon on 8/29/2015.
 */
public class GuideElementRecipe extends GuideElementAbstract {
    private static final ResourceLocation background = new ResourceLocation(Reference.PATH_ELEMENTS + "guide_recipe.png");
    private IRecipe recipe;

    @Override
    public void drawElement(int width, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        if (textAlign == 1) {
            GlStateManager.translate(marginLeft + this.width / 2 - 110 / 2, marginTop, 0);
        } else {
            GlStateManager.translate(marginLeft, marginTop, 0);
        }
        bindTexture(background);
        RenderUtils.applyColor(Reference.COLOR_MATTER);
        RenderUtils.drawPlane(8, 8, 0, 96, 96);
        if (recipe != null && !recipe.getIngredients().isEmpty()) {
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    int index = x + y * 3;
                    if (index < recipe.getIngredients().size()) {
                        Ingredient ingredient = recipe.getIngredients().get(index);
                        renderIngredient(ingredient, x, y);
                    }
                }
            }
        }
        GlStateManager.popMatrix();
    }

    private void renderIngredient(Ingredient ingredient, int x, int y) {
        if (ingredient != null) {
            ItemStack[] stacks = ingredient.getMatchingStacks();
            if (stacks.length > 0) {
                int stackIndex = (int) ((Minecraft.getMinecraft().world.getWorldTime() / 100) % (stacks.length));
                GlStateManager.pushMatrix();
                GlStateManager.translate(10 + x * 33, 9 + y * 33, 0);
                GlStateManager.scale(1.5, 1.5, 1.5);
                RenderUtils.renderStack(0, 0, stacks[stackIndex]);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    protected void loadContent(MOGuideEntry entry, Element element, int width, int height) {

        ItemStack output;
        if (element.hasAttribute("item")) {
            output = shortCodeToStack(decodeShortcode(element.getAttribute("item")));
        } else {
            output = entry.getStackIcons()[0];
        }

        for (IRecipe recipe : ForgeRegistries.RECIPES.getValuesCollection()) {
            if (ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), output)) {
                this.recipe = recipe;
                break;
            }
        }

        this.height = 100;
        this.width = 100;
    }
}