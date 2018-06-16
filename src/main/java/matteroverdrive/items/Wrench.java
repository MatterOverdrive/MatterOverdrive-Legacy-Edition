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

import matteroverdrive.api.wrench.IDismantleable;
import matteroverdrive.api.wrench.IWrenchable;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by Simeon on 5/19/2015.
 */
public class Wrench extends MOBaseItem {
    public Wrench(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        IBlockState state = world.getBlockState(pos);
        EnumActionResult result = EnumActionResult.PASS;
        if (!state.getBlock().isAir(state, world, pos)) {
            PlayerInteractEvent e = new PlayerInteractEvent.RightClickBlock(player, hand, pos, side, new Vec3d(hitX, hitY, hitZ));
            if (MinecraftForge.EVENT_BUS.post(e) || e.getResult() == Event.Result.DENY) {
                return EnumActionResult.FAIL;
            }
            if (player.isSneaking() && state.getBlock() instanceof IDismantleable && ((IDismantleable) state.getBlock()).canDismantle(player, world, pos)) {
                if (!world.isRemote) {
                    ((IDismantleable) state.getBlock()).dismantleBlock(player, world, pos, false);
                }
                result = EnumActionResult.SUCCESS;
            }
            if (state.getBlock() instanceof IWrenchable && !world.isRemote) {
                result = ((IWrenchable) state.getBlock()).onWrenchHit(stack, player, world, pos, side, hitX, hitY, hitZ) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
            } else if (!player.isSneaking() && state.getBlock().rotateBlock(world, pos, side)) {
                result = EnumActionResult.SUCCESS;
            }
        }
        if (result == EnumActionResult.SUCCESS)
            player.swingArm(hand);
        return result;
    }

    @Override
    public boolean hasDetails(ItemStack stack) {
        return true;
    }
}
