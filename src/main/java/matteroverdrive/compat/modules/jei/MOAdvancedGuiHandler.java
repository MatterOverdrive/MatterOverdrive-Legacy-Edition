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

import matteroverdrive.gui.MOGuiBase;
import mezz.jei.api.gui.IAdvancedGuiHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shadowfacts
 */
public class MOAdvancedGuiHandler implements IAdvancedGuiHandler<MOGuiBase> {

    @Nonnull
    @Override
    public Class<MOGuiBase> getGuiContainerClass() {
        return MOGuiBase.class;
    }

    @Nullable
    @Override
    public List<Rectangle> getGuiExtraAreas(MOGuiBase gui) {
        List<Rectangle> areas = new ArrayList<>();

        if (gui.getSidePannel().isOpen()) {
            areas.add(new Rectangle(gui.getSidePannel().getPosX() + gui.getGuiLeft(), gui.getSidePannel().getPosY() + gui.getGuiTop(), gui.getSidePannel().getWidth(), gui.getSidePannel().getHeight()));
        }

        gui.getElements().stream()
                .map(e -> new Rectangle(e.getPosX(), e.getPosY(), e.getWidth(), e.getHeight()))
                .forEach(areas::add);
        return areas;
    }

}
