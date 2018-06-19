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

import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.tile.TileEntityMachineStarMap;

/**
 * Created by Simeon on 6/28/2015.
 */
public class PagePlanetStats extends ElementBaseGroup implements IListHandler {
    TileEntityMachineStarMap starMap;

    public PagePlanetStats(GuiStarMap gui, int posX, int posY, int width, int height, TileEntityMachineStarMap starMap) {
        super(gui, posX, posY, width, height);
        this.starMap = starMap;
    }

    @Override
    public void init() {
        super.init();
    }


    @Override
    public void ListSelectionChange(String name, int selected) {

    }
}
