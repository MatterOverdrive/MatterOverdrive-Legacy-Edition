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

import matteroverdrive.items.includes.MOItemEnergyContainer;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Battery extends MOItemEnergyContainer {
    int capacity;
    int input;
    int output;

    public Battery(String name, int capacity, int input, int output) {
        super(name);
        this.capacity = capacity;
        this.input = input;
        this.output = output;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getInput() {
        return input;
    }

    @Override
    public int getOutput() {
        return output;
    }

    @Override
    public void addDetails(ItemStack stack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        super.addDetails(stack, player, worldIn, infos);
        infos.add(TextFormatting.GRAY + MOStringHelper.translateToLocal("gui.tooltip.energy.io") + ": " + getInput() + "/" + getOutput() + MOEnergyHelper.ENERGY_UNIT + "/t");
    }
}