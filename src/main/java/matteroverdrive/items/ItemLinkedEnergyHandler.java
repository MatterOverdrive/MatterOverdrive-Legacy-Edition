package matteroverdrive.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemLinkedEnergyHandler implements IEnergyStorage {
    private static String STORED = "stored";
    private static String CAPACITY = "capacity";
    private static String MAX_EXTRACT = "maxExtract";
    private static String MAX_RECEIVE = "maxReceive";
    private ItemStack stack;
    private NBTTagCompound energy;

    public ItemLinkedEnergyHandler(ItemStack stack, int capacity, int maxReceive, int maxExtract) {
        this.stack = stack;
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        this.energy = new NBTTagCompound();
        this.energy.setInteger(STORED, 0);
        this.energy.setInteger(CAPACITY, capacity);
        this.energy.setInteger(MAX_EXTRACT, maxExtract);
        this.energy.setInteger(MAX_RECEIVE, maxReceive);
        stack.getTagCompound().setTag("energy", energy);
    }

    public void setFull() {
        setEnergy(this.getMaxEnergyStored());
    }

    public void setEnergy(int energy) {
        if (energy > getMaxEnergyStored())
            energy = getMaxEnergyStored();
        this.energy.setInteger(STORED, energy);
    }

    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!this.canReceive()) {
            return 0;
        } else {
            int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(this.energy.getInteger(MAX_RECEIVE), maxReceive));
            if (!simulate) {
                this.energy.setInteger(STORED, this.energy.getInteger(STORED) + energyReceived);
            }
            return energyReceived;
        }
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!this.canExtract()) {
            return 0;
        } else {
            int energyExtracted = Math.min(getEnergyStored(), Math.min(this.energy.getInteger(MAX_EXTRACT), maxExtract));
            if (!simulate) {
                this.energy.setInteger(STORED, this.energy.getInteger(STORED) - energyExtracted);
            }
            return energyExtracted;
        }
    }

    @Override
    public int getEnergyStored() {
        return this.energy.getInteger(STORED);
    }

    @Override
    public int getMaxEnergyStored() {
        return this.energy.getInteger(CAPACITY);
    }

    @Override
    public boolean canExtract() {
        return energy.getInteger(MAX_EXTRACT) > 0;
    }

    @Override
    public boolean canReceive() {
        return energy.getInteger(MAX_RECEIVE) > 0;
    }
}