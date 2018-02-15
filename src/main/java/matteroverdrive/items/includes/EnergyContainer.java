package matteroverdrive.items.includes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyContainer extends EnergyStorage implements INBTSerializable<NBTTagCompound> {
    ItemStack stack;

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

    public EnergyContainer setItemStack(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            NBTTagCompound tag = serializeNBT();
            stack.setTagCompound(tag);
        }

        deserializeNBT(stack.getTagCompound());
        this.stack = stack;
        return this;
    }

    @Override
    public int getEnergyStored() {
        if (stack != null) {
            return stack.getTagCompound().getInteger("mo_energystored");
        }

        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        if (stack != null) {
            return stack.getTagCompound().getInteger("mo_capacity");
        }

        return capacity;
    }

    public int getMaxExtract() {
        if (stack != null) {
            return stack.getTagCompound().getInteger("mo_maxextract");
        }

        return maxExtract;
    }

    public int getMaxReceive() {
        if (stack != null) {
            return stack.getTagCompound().getInteger("mo_maxreceive");
        }

        return maxReceive;
    }

    public void setEnergy(int energy) {
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

        int energyReceived = Math.min(capacity - energy, Math.min(getMaxReceive(), amount));

        if (!simulate) {
            if (stack != null && energyReceived != 0) {
                stack.getTagCompound().setInteger("mo_energystored", getEnergyStored() + energyReceived);
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
                stack.getTagCompound().setInteger("mo_energystored", getEnergyStored() - energyExtracted);
            } else {
                energy -= energyExtracted;
            }
        }

        return energyExtracted;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("mo_energystored", energy);
        tag.setInteger("mo_capacity", capacity);
        tag.setInteger("mo_maxreceive", maxReceive);
        tag.setInteger("mo_maxextract", maxExtract);
        return tag;
    }

    public void deserializeNBT(NBTTagCompound tag) {
        energy = tag.getInteger("mo_energystored");
        capacity = tag.getInteger("mo_capacity");
        maxReceive = tag.getInteger("mo_maxreceive");
        maxExtract = tag.getInteger("mo_maxextract");
    }

    public void setMaxEnergy(int max) {
        this.capacity = max;
    }
}