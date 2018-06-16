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

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Simeon on 4/14/2015.
 */
public class MOInventoryHelper {

    public static void setInventorySlotContents(@Nonnull ItemStack container, int slot, @Nonnull ItemStack stack) {
        if (StackUtils.isNullOrEmpty(stack)) {
            if (!container.hasTagCompound()) {
                container.setTagCompound(new NBTTagCompound());
            }
            container.getTagCompound().setTag("Slot" + slot, new NBTTagCompound());
        } else {
            NBTTagCompound itemTag = new NBTTagCompound();
            stack.writeToNBT(itemTag);
            if (!container.hasTagCompound()) {
                container.setTagCompound(new NBTTagCompound());
            }
            container.getTagCompound().setTag("Slot" + slot, itemTag);
        }
    }

    @Nonnull
    public static ItemStack decrStackSize(ItemStack container, int slot, int amount) {
        if (container.getTagCompound().getCompoundTag("Slot" + slot) == null || container.getTagCompound().getCompoundTag("Slot" + slot).hasNoTags()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = new ItemStack(container.getTagCompound().getCompoundTag("Slot" + slot));
        ItemStack retStack = stack.splitStack(amount);
        if (stack.getCount() <= 0) {
            container.getTagCompound().setTag("Slot" + slot, new NBTTagCompound());
        } else {
            NBTTagCompound itemTag = new NBTTagCompound();
            stack.writeToNBT(itemTag);
            container.getTagCompound().setTag("Slot" + slot, itemTag);
        }
        return retStack;
    }

    @Nonnull
    public static ItemStack getStackInSlot(ItemStack container, int slot) {
        if (!container.hasTagCompound() || container.getTagCompound().getCompoundTag("Slot" + slot) == null || container.getTagCompound().getCompoundTag("Slot" + slot).hasNoTags()) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(container.getTagCompound().getCompoundTag("Slot" + slot));
    }

    public static List<ItemStack> getStacks(ItemStack container) {
        if (!container.hasTagCompound()) {
            return Collections.emptyList();
        }

        List<ItemStack> itemStacks = new ArrayList<>();

        for (String s : container.getTagCompound().getKeySet()) {
            if (s.startsWith("Slot")) {
                NBTBase nbtbase = container.getTagCompound().getTag(s);
                if (nbtbase instanceof NBTTagCompound) {
                    ItemStack stack = new ItemStack((NBTTagCompound) nbtbase);
                    if (!StackUtils.isNullOrEmpty(stack)) {
                        itemStacks.add(stack);
                    }
                }
            }
        }
        return itemStacks;
    }

    public static ItemStack addItemInContainer(Container container, ItemStack itemStack) {
        for (int i = 0; i < container.inventorySlots.size(); i++) {
            if (container.getSlot(i).isItemValid(itemStack)) {
                if (StackUtils.isNullOrEmpty(container.getSlot(i).getStack())) {
                    container.getSlot(i).putStack(itemStack);
                    if (itemStack.getCount() > itemStack.getMaxStackSize()) {
                        itemStack.setCount(itemStack.getMaxStackSize());
                    } else {
                        return null;
                    }
                } else if (ItemStack.areItemStacksEqual(container.getSlot(i).getStack(), itemStack) && container.getSlot(i).getStack().getCount() < container.getSlot(i).getStack().getMaxStackSize()) {
                    int newStackSize = Math.min(container.getSlot(i).getStack().getCount() + itemStack.getCount(), container.getSlot(i).getStack().getMaxStackSize());
                    int leftStackSize = container.getSlot(i).getStack().getCount() + itemStack.getCount() - newStackSize;
                    container.getSlot(i).getStack().setCount(newStackSize);
                    if (leftStackSize <= 0) {
                        return null;
                    }

                    itemStack.setCount(newStackSize);
                }
            }
        }
        return itemStack;
    }

    public static ItemStack insertItemStackIntoInventory(IInventory inventory, ItemStack itemstack, EnumFacing side) {
        if (itemstack != null && inventory != null) {
            int var3 = itemstack.getCount();
            if (inventory instanceof ISidedInventory) {
                ISidedInventory var4 = (ISidedInventory) inventory;
                int[] var5 = var4.getSlotsForFace(side);
                if (var5 == null) {
                    return itemstack;
                }

                int var6;
                for (var6 = 0; var6 < var5.length && itemstack != null; ++var6) {
                    if (var4.canInsertItem(var5[var6], itemstack, side)) {
                        ItemStack var7 = inventory.getStackInSlot(var5[var6]);
                        if (ItemStack.areItemStacksEqual(itemstack, var7)) {
                            itemstack = addToOccupiedInventorySlot(var4, var5[var6], itemstack, var7);
                        }
                    }
                }

                for (var6 = 0; var6 < var5.length && itemstack != null; ++var6) {
                    if (inventory.getStackInSlot(var5[var6]) == null && var4.canInsertItem(var5[var6], itemstack, side)) {
                        itemstack = addToEmptyInventorySlot(var4, var5[var6], itemstack);
                    }
                }
            } else {
                int var8 = inventory.getSizeInventory();

                int var9;
                for (var9 = 0; var9 < var8 && itemstack != null; ++var9) {
                    ItemStack var10 = inventory.getStackInSlot(var9);
                    if (ItemStack.areItemStacksEqual(itemstack, var10)) {
                        itemstack = addToOccupiedInventorySlot(inventory, var9, itemstack, var10);
                    }
                }

                for (var9 = 0; var9 < var8 && itemstack != null; ++var9) {
                    if (StackUtils.isNullOrEmpty(inventory.getStackInSlot(var9))) {
                        itemstack = addToEmptyInventorySlot(inventory, var9, itemstack);
                    }
                }
            }

            if (itemstack == null || itemstack.getCount() != var3) {
                inventory.markDirty();
            }

            return itemstack;
        } else {
            return null;
        }
    }

    public static ItemStack addToOccupiedInventorySlot(IInventory inventory, int slot, ItemStack one, ItemStack two) {
        int maxSize = Math.min(inventory.getInventoryStackLimit(), one.getMaxStackSize());
        if (one.getCount() + two.getCount() > maxSize) {
            int remanningSize = maxSize - two.getCount();
            two.setCount(maxSize);
            one.shrink(remanningSize);
            inventory.setInventorySlotContents(slot, two);
            return one;
        } else {
            two.grow(Math.min(one.getCount(), maxSize));
            inventory.setInventorySlotContents(slot, two);
            return maxSize >= one.getCount() ? ItemStack.EMPTY : one.splitStack(one.getCount() - maxSize);
        }
    }

    public static ItemStack addToEmptyInventorySlot(IInventory inventory, int slot, ItemStack itemStack) {
        if (!inventory.isItemValidForSlot(slot, itemStack)) {
            return itemStack;
        } else {
            int inventoryStackLimit = inventory.getInventoryStackLimit();
            ItemStack newItemStack = itemStack.copy();
            newItemStack.setCount(Math.min(itemStack.getCount(), inventoryStackLimit));
            inventory.setInventorySlotContents(slot, newItemStack);

            return inventoryStackLimit >= itemStack.getCount() ? null : itemStack.splitStack(itemStack.getCount() - inventoryStackLimit);
        }
    }

    public static boolean mergeItemStack(List<Slot> var0, ItemStack var1, int var2, int var3, boolean var4) {
        return mergeItemStack(var0, var1, var2, var3, var4, true);
    }

    public static boolean mergeItemStack(List<Slot> slots, ItemStack stack, int var2, int var3, boolean var4, boolean var5) {
        boolean var6 = false;
        int var7 = !var4 ? var2 : var3 - 1;
        int var8 = !var4 ? 1 : -1;
        Slot var9;
        ItemStack var10;
        int var11;
        if (stack.isStackable()) {
            for (; stack.getCount() > 0 && (!var4 && var7 < var3 || var4 && var7 >= var2); var7 += var8) {
                var9 = slots.get(var7);
                var10 = var9.getStack();
                if (var9.isItemValid(stack) && !StackUtils.isNullOrEmpty(stack) && var10.getItem().equals(stack.getItem()) && (!stack.getHasSubtypes() || stack.getItemDamage() == var10.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, var10)) {
                    var11 = var10.getCount() - stack.getCount();
                    var10.grow(stack.getCount());
                    int var12 = Math.min(stack.getMaxStackSize(), var9.getSlotStackLimit());
                    if (var11 <= var12) {
                        stack.setCount(0);
                        var10.setCount(var11);
                        var9.onSlotChanged();
                        var6 = true;
                    } else if (var10.getCount() < var12) {
                        stack.shrink(var12 - var10.getCount());
                        var10.setCount(var12);
                        var9.onSlotChanged();
                        var6 = true;
                    }
                }
            }
        }

        if (stack.getCount() > 0) {
            for (var7 = !var4 ? var2 : var3 - 1; stack.getCount() > 0 && (!var4 && var7 < var3 || var4 && var7 >= var2); var7 += var8) {
                var9 = slots.get(var7);
                var10 = var9.getStack();
                if (var9.isItemValid(stack) && StackUtils.isNullOrEmpty(var10)) {
                    var11 = var5 ? Math.min(stack.getMaxStackSize(), var9.getSlotStackLimit()) : var9.getSlotStackLimit();
                    var10 = stack.splitStack(Math.min(stack.getCount(), var11));
                    var9.putStack(var10);
                    var9.onSlotChanged();
                    var6 = true;
                }
            }
        }

        return var6;
    }
}
