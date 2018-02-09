package matteroverdrive.api;

import matteroverdrive.items.includes.EnergyContainer;
import net.minecraft.nbt.NBTTagCompound;

public class EmptyEnergyStorage extends EnergyContainer {
    public static EmptyEnergyStorage INSTANCE = new EmptyEnergyStorage();

    public EmptyEnergyStorage() {
        super(0);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return 0;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    @Override
    public void setFull() {

    }

    @Override
    public void setEnergy(int energy) {

    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {

    }
}