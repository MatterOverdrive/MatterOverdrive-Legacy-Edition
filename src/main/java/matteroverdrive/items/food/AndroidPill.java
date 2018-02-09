/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
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

package matteroverdrive.items.food;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Simeon on 7/12/2015.
 */
public class AndroidPill extends MOItemFood {
    public static final String[] names = new String[]{"red", "blue", "yellow"};

    public AndroidPill(String name) {
        super(name, 0, 0, false);
        setAlwaysEdible();
        hasSubtypes = true;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, @Nullable World worldIn, List<String> infos, ITooltipFlag flagIn) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            String[] infoList = MOStringHelper.translateToLocal(getUnlocalizedName(itemstack) + ".details").split("/n");
            for (String info : infoList) {
                infos.add(TextFormatting.GRAY + info);
            }
        } else {
            infos.add(MOStringHelper.MORE_INFO);
        }

        if (itemstack.getItemDamage() == 2) {
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(Minecraft.getMinecraft().player);
            if (androidPlayer != null && androidPlayer.isAndroid()) {
                infos.add(TextFormatting.GREEN + "XP:" + androidPlayer.getResetXPRequired() + "l");
            } else {
                infos.add(TextFormatting.RED + "Not an Android.");
            }
        }
    }

    public void addToDunguns() {
        // TODO: 3/24/2016 Find new way of adding dungon loot
        //ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(this,1),1,1,1));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return getUnlocalizedName() + "_" + names[MathHelper.clamp(itemStack.getItemDamage(), 0, names.length - 1)];
    }

    /*@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "pill_bottom");
        this.overlay = iconRegister.registerIcon(Reference.MOD_ID + ":" + "pill_top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }*/

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (isInCreativeTab(tab)) {
            list.add(new ItemStack(this, 1, 0));
            list.add(new ItemStack(this, 1, 1));
            list.add(new ItemStack(this, 1, 2));
        }
    }


    /*@Override
	public int getRenderPasses(int metadata)
    {
        return 2;
    }*/

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass)
    {
        if (pass == 1)
        {
            return overlay;
        }else
        {
            return itemIcon;
        }
    }*/

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(playerIn);
        if (androidPlayer == null)
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        if (stack.getItemDamage() >= 1) {
            if (!androidPlayer.isTurning() && androidPlayer.isAndroid()) {
                playerIn.setActiveHand(handIn);
                return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
            }
        } else {
            if (!androidPlayer.isAndroid() && !androidPlayer.isTurning()) {
                playerIn.setActiveHand(handIn);
                return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
            }
        }
        return ActionResult.newResult(EnumActionResult.FAIL, stack);
    }

    public void register() {
        setCreativeTab(MatterOverdrive.TAB_OVERDRIVE_FOOD);
        setRegistryName(new ResourceLocation(Reference.MOD_ID, this.getUnlocalizedName().substring(5)));
        GameData.register_impl(this);
    }

    @Override
    protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote) {
            return;
        }

        AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
        if (itemStack.getItemDamage() == 0) {
            androidPlayer.startConversion();
        } else if (itemStack.getItemDamage() == 1) {
            androidPlayer.setAndroid(false);
        } else if (itemStack.getItemDamage() == 2) {
            if (!androidPlayer.isTurning() && androidPlayer.isAndroid()) {
                int xpLevels = androidPlayer.resetUnlocked();
                player.addExperienceLevel(xpLevels);
            }
        }
    }
}