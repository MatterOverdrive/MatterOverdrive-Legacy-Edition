package matteroverdrive.items.includes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
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
        boolean hasTags = stack.hasTagCompound();
        if (!hasTags || !stack.getTagCompound().hasKey("energy")) {
            if (!hasTags) {
                stack.setTagCompound(new NBTTagCompound());
            }

            NBTTagCompound tag = stack.getTagCompound();
            tag.setTag("energy",  serializeNBT());
        } else {
            deserializeNBT(stack.getTagCompound().getCompoundTag("energy"));
        }

        this.stack = stack;
        return this;
    }

    private NBTTagCompound stackTag() {
        return (NBTTagCompound) stack.getTagCompound().getTag("energy");
    }

    @Override
    public int getEnergyStored() {
        if (stack != null) {
            return stackTag().getInteger("energy");
        }

        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        if (stack != null) {
            return stackTag().getInteger("capacity");
        }

        return capacity;
    }

    public int getMaxExtract() {
        if (stack != null) {
            return stackTag().getInteger("maxExtract");
        }

        return maxExtract;
    }

    public int getMaxReceive() {
        if (stack != null) {
            return stackTag().getInteger("maxReceive");
        }

        return maxReceive;
    }

    public void setEnergy(int energy) {
        if (stack != null) {
            stackTag().setInteger("energy", energy);

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
                stackTag().setInteger("energy", getEnergyStored() + energyReceived);
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
                stackTag().setInteger("energy", getEnergyStored() - energyExtracted);
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
            stackTag().setInteger("capacity", max);
            return;
        }

        this.capacity = max;
    }
}