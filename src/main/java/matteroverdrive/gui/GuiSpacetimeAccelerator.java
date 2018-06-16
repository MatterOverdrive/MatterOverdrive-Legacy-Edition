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

import matteroverdrive.container.ContainerFactory;
import matteroverdrive.gui.element.ElementMatterStored;
import matteroverdrive.gui.element.MOElementEnergy;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.tile.TileEntityMachineSpacetimeAccelerator;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 1/22/2016.
 */
public class GuiSpacetimeAccelerator extends MOGuiMachine<TileEntityMachineSpacetimeAccelerator> {
    private final ElementMatterStored matterStored;
    private final MOElementEnergy energy;

    public GuiSpacetimeAccelerator(InventoryPlayer inventoryPlayer, TileEntityMachineSpacetimeAccelerator machine) {
        super(ContainerFactory.createMachineContainer(machine, inventoryPlayer), machine);

        matterStored = new ElementMatterStored(this, 74, 39, machine.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null));
        energy = new MOElementEnergy(this, 100, 39, machine.getEnergyStorage());
    }

    @Override
    public void initGui() {
        super.initGui();
        pages.get(0).addElement(matterStored);
        pages.get(0).addElement(energy);

        AddMainPlayerSlots(this.inventorySlots, pages.get(0));
        AddHotbarPlayerSlots(this.inventorySlots, this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {
        super.drawGuiContainerBackgroundLayer(partialTick, x, y);
        int energyDrain = machine.getEnergyUsage();
        energy.setEnergyRequiredPerTick(-energyDrain);
        double matterDrain = machine.getMatterUsage();
        matterStored.setDrainPerTick(-matterDrain);
    }
}
