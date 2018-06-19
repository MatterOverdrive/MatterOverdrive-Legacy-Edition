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

package matteroverdrive.gui.pages.starmap;

import matteroverdrive.container.ContainerStarMap;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.StarmapHelper;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Created by Simeon on 6/15/2015.
 */
public class PagePlanetMenu extends ElementBaseGroup {

    private TileEntityMachineStarMap starMap;

    public PagePlanetMenu(MOGuiBase gui, int posX, int posY, int width, int height, ContainerStarMap starMapContainer, TileEntityMachineStarMap starMap) {
        super(gui, posX, posY, width, height);
        this.starMap = starMap;
    }

    @Override
    public void update(int mouseX, int mouseY, float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
        if (starMap.getPlanet() != null) {
            GlStateManager.pushMatrix();
            int width = getFontRenderer().getStringWidth(starMap.getPlanet().getSpaceBodyName());
            GlStateManager.translate(sizeY / 2 + width / 2, 16, 0);
            GlStateManager.scale(1, 1, 1);
            StarmapHelper.drawPlanetInfo(starMap.getPlanet(), starMap.getPlanet().getSpaceBodyName(), 12 - width / 2, 4);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void init() {
        super.init();
    }
}
