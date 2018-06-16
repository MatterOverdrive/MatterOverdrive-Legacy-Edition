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
package matteroverdrive.gui;

import matteroverdrive.container.ContainerSolarPanel;
import matteroverdrive.gui.element.MOElementEnergy;
import matteroverdrive.tile.TileEntityMachineSolarPanel;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 4/9/2015.
 */
public class GuiSolarPanel extends MOGuiMachine<TileEntityMachineSolarPanel> {
    MOElementEnergy energy;

    public GuiSolarPanel(InventoryPlayer inventoryPlayer, TileEntityMachineSolarPanel solarPanel) {
        super(new ContainerSolarPanel(inventoryPlayer, solarPanel), solarPanel);
        name = "solar_panel";
        energy = new MOElementEnergy(this, 117, 35, solarPanel.getEnergyStorage());
    }

    @Override
    public void initGui() {
        super.initGui();
        AddMainPlayerSlots(inventorySlots, pages.get(0));
        AddHotbarPlayerSlots(inventorySlots, this);
        elements.remove(slotsList);
        pages.get(0).addElement(energy);
    }

    @Override
    protected void updateElementInformation() {
        super.updateElementInformation();
        energy.setEnergyRequired(machine.getChargeAmount());
    }
}
