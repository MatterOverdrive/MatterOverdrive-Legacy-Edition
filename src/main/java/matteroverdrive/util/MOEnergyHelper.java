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
package matteroverdrive.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class MOEnergyHelper {
    public static String ENERGY_UNIT = "FE";

    public static String formatEnergy(long energy, long capacity) {
        return MOStringHelper.formatNumber(energy) + " / " + MOStringHelper.formatNumber(capacity) + ENERGY_UNIT;
    }

    public static String formatEnergy(long energy) {
        return formatEnergy("Charge: ", energy);
    }

    public static String formatEnergy(String prefix, long energy) {
        return (prefix != null ? prefix : "") + MOStringHelper.formatNumber(energy) + ENERGY_UNIT;
    }

    public static boolean extractExactAmount(IEnergyStorage provider, int amount, boolean simulate) {
        int hasEnergy = provider.getEnergyStored();
        if (hasEnergy >= amount) {
            while (amount > 0) {
                if (provider.extractEnergy(amount, true) >= 0) {
                    amount -= provider.extractEnergy(amount, simulate);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static ItemStack setDefaultEnergyTag(ItemStack itemStack, int energy) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        itemStack.getTagCompound().setInteger("Energy", energy);
        return itemStack;
    }

    public static int extractEnergyFromContainer(ItemStack itemStack, int amount, boolean simulate) {
        return isEnergyContainerItem(itemStack) ? (itemStack.getCapability(CapabilityEnergy.ENERGY, null)).extractEnergy(amount, simulate) : 0;
    }

    public static int insertEnergyIntoContainer(ItemStack itemStack, int amount, boolean simulate) {
        return isEnergyContainerItem(itemStack) ? (itemStack.getCapability(CapabilityEnergy.ENERGY, null)).receiveEnergy(amount, simulate) : 0;
    }

    public static boolean isEnergyContainerItem(ItemStack itemStack) {
        return !itemStack.isEmpty() && itemStack.hasCapability(CapabilityEnergy.ENERGY, null);
    }

    public static int insertEnergyIntoAdjacentEnergyReceiver(TileEntity tileEntity, EnumFacing side, int amount, boolean simulate) {
        TileEntity tile2 = tileEntity.getWorld().getTileEntity(tileEntity.getPos().offset(side));
        if (tile2 == null)
            return 0;

        return tile2.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite()) ? tile2.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).receiveEnergy(amount, simulate) : 0;
    }
}