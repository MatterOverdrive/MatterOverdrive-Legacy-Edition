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
import net.minecraft.util.ResourceLocation;
import org.w3c.dom.Element;

/**
 * Created by Simeon on 8/29/2015.
 */
public class GuideElementRecipe extends GuideElementAbstract {
    private static final ResourceLocation background = new ResourceLocation(Reference.PATH_ELEMENTS + "guide_recipe.png");

    @Override
    protected void loadContent(MOGuideEntry entry, Element element, int width, int height) {

    }

    @Override
    public void drawElement(int width, int mouseX, int mouseY) {

    }
}
