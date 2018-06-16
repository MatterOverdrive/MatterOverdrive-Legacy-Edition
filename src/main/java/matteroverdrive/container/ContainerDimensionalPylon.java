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
package matteroverdrive.container;

import matteroverdrive.machines.dimensional_pylon.TileEntityMachineDimensionalPylon;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 2/12/2016.
 */
public class ContainerDimensionalPylon extends ContainerMachine<TileEntityMachineDimensionalPylon> {
    private int energyGenPerTick;
    private int matterDrainPerSec;

    public ContainerDimensionalPylon(InventoryPlayer inventory, TileEntityMachineDimensionalPylon machine) {
        super(inventory, machine);
    }

    @Override
    public void addListener(IContainerListener icrafting) {
        super.addListener(icrafting);
        icrafting.sendWindowProperty(this, 1, this.machine.getEnergyGenPerTick());
        icrafting.sendWindowProperty(this, 2, this.machine.getMatterDrainPerSec());
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : this.listeners) {
            if (this.energyGenPerTick != this.machine.getEnergyGenPerTick()) {
                listener.sendWindowProperty(this, 1, this.machine.getEnergyGenPerTick());
            }
            if (this.matterDrainPerSec != this.machine.getMatterDrainPerSec()) {
                listener.sendWindowProperty(this, 2, this.machine.getMatterDrainPerSec());
            }
        }

        this.energyGenPerTick = this.machine.getEnergyGenPerTick();
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int newValue) {
        super.updateProgressBar(slot, newValue);
        switch (slot) {
            case 1:
                energyGenPerTick = newValue;
                break;
            case 2:
                matterDrainPerSec = newValue;
        }
    }

    @Override
    public void init(InventoryPlayer inventory) {
        addAllSlotsFromInventory(machine.getInventoryContainer());
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, false, true);
    }

    public int getEnergyGenPerTick() {
        return energyGenPerTick;
    }

    public int getMatterDrainPerSec() {
        return matterDrainPerSec;
    }
}
