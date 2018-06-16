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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.matter.IMatterItem;
import matteroverdrive.api.matter.IRecyclable;
import matteroverdrive.items.includes.MOItemOre;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class MatterDust extends MOItemOre implements IRecyclable, IMatterItem {
    final boolean isRefined;

    public MatterDust(String name, String oreDict, boolean refined) {
        super(name, oreDict);
        isRefined = refined;
    }

    @Override
    public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        if (!isRefined) {
            infos.add(TextFormatting.BLUE + "Potential Matter: " + MatterHelper.formatMatter(itemstack.getItemDamage()));
        }
    }

    public int getDamage(ItemStack stack) {
        TagCompountCheck(stack);
        return stack.getTagCompound().getInteger("Matter");
    }

    public void setMatter(ItemStack itemStack, int matter) {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setInteger("Matter", matter);
    }

    @Override
    public boolean hasDetails(ItemStack itemStack) {
        return !isRefined;
    }

    @Override
    public ItemStack getOutput(ItemStack from) {
        ItemStack newItemStack = new ItemStack(MatterOverdrive.ITEMS.matter_dust_refined);
        MatterOverdrive.ITEMS.matter_dust_refined.setMatter(newItemStack, from.getItemDamage());
        return newItemStack;
    }

    @Override
    public int getRecycleMatter(ItemStack stack) {
        return stack.getItemDamage();
    }

    @Override
    public boolean canRecycle(ItemStack stack) {
        return stack.getItem() instanceof MatterDust && !((MatterDust) stack.getItem()).isRefined;
    }

    @Override
    public int getMatter(ItemStack itemStack) {
        return itemStack.getItem() instanceof MatterDust && ((MatterDust) itemStack.getItem()).isRefined ? itemStack.getItemDamage() : 0;
    }
}
