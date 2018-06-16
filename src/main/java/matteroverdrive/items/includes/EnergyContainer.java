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
package matteroverdrive.items.includes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nullable;

public class EnergyContainer extends EnergyStorage implements INBTSerializable<NBTTagCompound> {
    private ItemStack stack;

    public EnergyContainer(int capacity) {
        super(capacity);
    }

    public EnergyContainer(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public EnergyContainer(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public EnergyContainer(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setFull() {
        setEnergy(this.capacity);
    }

    @Nullable
    public ItemStack getItemStack() {
        return this.stack;
    }

    public EnergyContainer setItemStack(ItemStack stack) {
        boolean hasTags = stack.hasTagCompound();
        if (!hasTags || !stack.getTagCompound().hasKey("energy")) {
            if (!hasTags) {
                stack.setTagCompound(new NBTTagCompound());
            }

            NBTTagCompound tag = stack.getTagCompound();
            tag.setTag("energy", serializeNBT());
        } else {
            deserializeNBT(stack.getTagCompound().getCompoundTag("energy"));
        }

        this.stack = stack;
        return this;
    }

    private NBTTagCompound getStackEnergyTag() {
        return stack.getTagCompound().getCompoundTag("energy");
    }

    @Override
    public int getEnergyStored() {
        if (stack != null) {
            return getStackEnergyTag().getInteger("energy");
        }

        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        if (stack != null) {
            return getStackEnergyTag().getInteger("capacity");
        }

        return capacity;
    }

    public int getMaxExtract() {
        if (stack != null) {
            return getStackEnergyTag().getInteger("maxExtract");
        }

        return maxExtract;
    }

    public int getMaxReceive() {
        if (stack != null) {
            return getStackEnergyTag().getInteger("maxReceive");
        }

        return maxReceive;
    }

    public void setEnergy(int energy) {
        if (stack != null) {
            getStackEnergyTag().setInteger("energy", energy);

            if (getEnergyStored() > getMaxEnergyStored()) {
                setFull();
            }

            return;
        }

        this.energy = energy;
        if (this.energy > this.capacity)
            setFull();
    }

    @Override
    public boolean canExtract() {
        return getMaxExtract() > 0;
    }

    @Override
    public boolean canReceive() {
        return getMaxReceive() > 0;
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(getMaxReceive(), amount));

        if (!simulate) {
            if (stack != null && energyReceived != 0) {
                getStackEnergyTag().setInteger("energy", getEnergyStored() + energyReceived);
            } else {
                energy += energyReceived;
            }
        }

        return energyReceived;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(getEnergyStored(), Math.min(getMaxExtract(), amount));

        if (!simulate) {
            if (stack != null && energyExtracted != 0) {
                getStackEnergyTag().setInteger("energy", getEnergyStored() - energyExtracted);
            } else {
                energy -= energyExtracted;
            }
        }

        return energyExtracted;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("energy", energy);
        tag.setInteger("capacity", capacity);
        tag.setInteger("maxReceive", maxReceive);
        tag.setInteger("maxExtract", maxExtract);
        return tag;
    }

    public void deserializeNBT(NBTTagCompound tag) {
        energy = tag.getInteger("energy");
        capacity = tag.getInteger("capacity");
        maxReceive = tag.getInteger("maxReceive");
        maxExtract = tag.getInteger("maxExtract");
    }

    public void setMaxEnergy(int max) {
        if (stack != null) {
            getStackEnergyTag().setInteger("capacity", max);
            return;
        }

        this.capacity = max;
    }
}