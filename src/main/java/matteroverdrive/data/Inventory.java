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
package matteroverdrive.data;

import matteroverdrive.data.inventory.Slot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Simeon on 3/16/2015.
 */
public class Inventory implements IInventory {
    final NonNullList<Slot> slots;
    String name;
    IUsableCondition usableCondition;

    //region Constructors
    public Inventory(String name) {
        this(name, new ArrayList<>());
    }

    public Inventory(String name, Collection<Slot> slots) {
        this(name, slots, null);
    }

    public Inventory(String name, Collection<Slot> slots, IUsableCondition usableCondition) {
        this.slots = NonNullList.create();
        this.slots.addAll(slots);
        this.name = name;
        this.usableCondition = usableCondition;
    }

    @Override
    public boolean isEmpty() {
        return slots.isEmpty();
    }    //endregion

    public int AddSlot(Slot slot) {
        if (slots.add(slot)) {
            slot.setId(slots.size() - 1);
            return slots.size() - 1;
        }
        return 0;
    }

    public void setUsableCondition(IUsableCondition condition) {
        this.usableCondition = condition;
    }

    public void readFromNBT(NBTTagCompound compound) {
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");
            if (nbttagcompound1.hasKey("id")) {
                setInventorySlotContents(b0, new ItemStack(nbttagcompound1));
            } else {
                setInventorySlotContents(b0, ItemStack.EMPTY);
            }
        }
    }

    public void writeToNBT(NBTTagCompound compound, boolean toDisk) {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i) {
            writeSlotToNBT(nbttaglist, i, toDisk);
        }

        if (nbttaglist.tagCount() > 0) {
            compound.setTag("Items", nbttaglist);
        }
    }

    protected void writeSlotToNBT(NBTTagList nbttaglist, int slotId, boolean toDisk) {
        Slot slot = getSlot(slotId);
        if (slot != null) {
            if (toDisk && !slot.getItem().isEmpty()) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) slotId);
                if (!slot.getItem().isEmpty()) {
                    slot.getItem().writeToNBT(nbttagcompound1);
                }
                nbttaglist.appendTag(nbttagcompound1);
            } else if (!toDisk && slot.sendsToClient()) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) slotId);
                if (!slot.getItem().isEmpty()) {
                    slot.getItem().writeToNBT(nbttagcompound1);
                }
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return slots.size();
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return slots.get(slot).getItem();
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int slotId, int size) {
        Slot slot = getSlot(slotId);
        if (slot != null && !slot.getItem().isEmpty()) {
            ItemStack itemstack;

            if (slot.getItem().getCount() <= size) {
                itemstack = slot.getItem();
                slot.setItem(ItemStack.EMPTY);

                return itemstack;
            } else {
                itemstack = slot.getItem().splitStack(size);

                if (slot.getItem().getCount() == 0) {
                    slot.setItem(ItemStack.EMPTY);
                }

                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemStack = getSlot(index).getItem();
        getSlot(index).setItem(ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack item) {
        getSlot(slot).setItem(item);

        if (!item.isEmpty() && item.getCount() > this.getInventoryStackLimit()) {
            item.setCount(this.getInventoryStackLimit());
        }
    }

    public void addItem(ItemStack itemStack) {
        for (int i = 0; i < slots.size(); i++) {
            Slot slot = getSlot(i);
            if (slot.isValidForSlot(itemStack)) {
                if (slot.getItem().isEmpty()) {
                    slot.setItem(itemStack);
                    return;
                } else if (ItemStack.areItemStacksEqual(slot.getItem(), itemStack) && slot.getItem().getCount() < slot.getItem().getMaxStackSize()) {
                    int newStackSize = Math.min(slot.getItem().getCount() + itemStack.getCount(), slot.getItem().getMaxStackSize());
                    int leftStackSize = slot.getItem().getCount() + itemStack.getCount() - newStackSize;
                    slot.getItem().setCount(newStackSize);
                    if (leftStackSize <= 0) {
                        return;
                    }

                    itemStack.setCount(newStackSize);
                }
            }
        }
    }

    public void clearItems() {
        for (Slot slot : slots) {
            slot.setItem(ItemStack.EMPTY);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasCustomName() {
        return name != null && !name.isEmpty();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(this.name);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;

    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack item) {
        if (slotID >= 0 && slotID < getSizeInventory() && getSlot(slotID) != null) {
            Slot slot = getSlot(slotID);
            if (!slot.getItem().isEmpty()) {
                return slot.getItem().getCount() <= slot.getMaxStackSize() && slot.isValidForSlot(item);
            }
            return slot.isValidForSlot(item);
        }
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (Slot slot : slots) {
            slot.setItem(ItemStack.EMPTY);
        }
    }

    public Slot getSlot(int slotID) {
        return slots.get(slotID);
    }

    public int getLastSlotId() {
        return slots.size() - 1;
    }

    public List<Slot> getSlots() {
        return slots;
    }
}
