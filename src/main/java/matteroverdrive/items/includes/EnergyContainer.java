package matteroverdrive.items.includes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyContainer extends EnergyStorage implements INBTSerializable<NBTTagCompound> {
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

    public void setEnergy(int energy) {
        this.energy = energy;
        if (this.energy > this.capacity)
            setFull();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("energy", energy);
        tag.setInteger("capacity", capacity);
        tag.setInteger("maxReceive", maxReceive);
        tag.setInteger("maxExtract", maxExtract);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        energy = tag.getInteger("energy");
        capacity = tag.getInteger("capacity");
        maxReceive = tag.getInteger("maxReceive");
        maxExtract = tag.getInteger("maxExtract");
    }

    public void setMaxEnergy(int max) {
        this.capacity = max;
    }
}