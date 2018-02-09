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

package matteroverdrive.data;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.items.includes.EnergyContainer;
import matteroverdrive.tile.MOTileEntityMachineEnergy;

/**
 * Created by Simeon on 8/7/2015.
 */
public class MachineEnergyStorage<T extends MOTileEntityMachineEnergy> extends EnergyContainer {
    protected final T machine;

    public MachineEnergyStorage(int capacity, T machine) {
        super(capacity);
        this.machine = machine;
    }

    public MachineEnergyStorage(int capacity, int maxTransfer, T machine) {
        super(capacity, maxTransfer);
        this.machine = machine;
    }

    public MachineEnergyStorage(int capacity, int maxReceive, int maxExtract, T machine) {
        super(capacity, maxReceive, maxExtract);
        this.machine = machine;
    }

    public MachineEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, T machine) {
        super(capacity, maxReceive, maxExtract, energy);
        this.machine = machine;
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public int modifyEnergyStored(int amount) {
        if (amount > 0) {
            return receiveEnergy(amount, false);
        } else if (amount < 0) {
            return extractEnergy(-amount, false);
        } else {
            return 0;
        }
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        int ex = super.receiveEnergy(amount, simulate);
        if(ex!=0)
        machine.UpdateClientPower();
        return ex;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        int ex = super.extractEnergy(amount, simulate);
        if(ex!=0)
        machine.UpdateClientPower();
        return ex;
    }

    public long getInputRate() {
        return (long) (super.maxReceive * machine.getUpgradeMultiply(UpgradeTypes.PowerTransfer));
    }

    public long getOutputRate() {
        return (long) (super.maxExtract * machine.getUpgradeMultiply(UpgradeTypes.PowerTransfer));
    }

    @Override
    public int getMaxEnergyStored() {
        return (int)(super.getMaxEnergyStored()*machine.getUpgradeMultiply(UpgradeTypes.PowerStorage));
    }

    public int getMaxEnergyStoredUnaltered() {
        return super.getMaxEnergyStored();
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
