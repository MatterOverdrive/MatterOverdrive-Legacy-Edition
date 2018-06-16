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
package matteroverdrive.items;

import matteroverdrive.items.includes.EnergyContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class CreativeBattery extends Battery {
    public CreativeBattery(String name) {
        super(name, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public ICapabilitySerializable<NBTTagCompound> createProvider(ItemStack stack) {
        return new InfiniteProvider(getCapacity(), getInput(), getOutput());
    }

    @Override
    protected boolean addPoweredItem() {
        return false;
    }

    public static class InfiniteProvider implements ICapabilitySerializable<NBTTagCompound> {

        private EnergyContainer container;

        public InfiniteProvider(int capacity, int input, int output) {
            container = new EnergyContainer(capacity, input, output, 0) {
                @Override
                public int receiveEnergy(int maxReceive, boolean simulate) {
                    return 0;
                }

                @Override
                public int extractEnergy(int maxExtract, boolean simulate) {
                    return Math.max(maxExtract, this.maxExtract);
                }

                @Override
                public int getEnergyStored() {
                    return Integer.MAX_VALUE;
                }
            };
        }

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityEnergy.ENERGY;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == CapabilityEnergy.ENERGY) {
                return CapabilityEnergy.ENERGY.cast(container);
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return container.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound tag) {
            container.deserializeNBT(tag);
        }
    }
}