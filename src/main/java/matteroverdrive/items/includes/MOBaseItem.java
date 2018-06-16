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
package matteroverdrive.items.includes;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.items.ItemBase;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

public class MOBaseItem extends ItemBase {
    public MOBaseItem(String name) {
        super(name);
        this.setCreativeTab(MatterOverdrive.TAB_OVERDRIVE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (hasDetails(stack)) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || flagIn.isAdvanced()) {
                addDetails(stack, Minecraft.getMinecraft().player, worldIn, tooltip);
            } else {
                tooltip.add(MOStringHelper.MORE_INFO);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        if (MOStringHelper.hasTranslation(getUnlocalizedName(itemstack) + ".details")) {
            String[] infoList = MOStringHelper.translateToLocal(getUnlocalizedName(itemstack) + ".details").split("/n");
            for (String info : infoList) {
                infos.add(TextFormatting.GRAY + info);
            }
        }
    }

    public void InitTagCompount(ItemStack stack) {
        stack.setTagCompound(new NBTTagCompound());
    }

    public void TagCompountCheck(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            InitTagCompount(stack);
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean hasDetails(ItemStack stack) {
        return false;
    }
}