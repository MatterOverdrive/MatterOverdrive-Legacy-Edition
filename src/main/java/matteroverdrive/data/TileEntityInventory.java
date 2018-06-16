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
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Simeon on 5/26/2015.
 */
public class TileEntityInventory extends Inventory {
    final TileEntity entity;

    public TileEntityInventory(TileEntity entity, String name) {
        this(entity, name, new ArrayList<>());
    }

    public TileEntityInventory(TileEntity entity, String name, Collection<Slot> slots) {
        this(entity, name, slots, null);
    }

    public TileEntityInventory(TileEntity entity, String name, Collection<Slot> slots, IUsableCondition usableCondition) {
        super(name, slots, usableCondition);
        this.entity = entity;
    }

    @Override
    public void markDirty() {
        if (this.entity != null) {
            this.entity.markDirty();
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (usableCondition != null) {
            return usableCondition.usableByPlayer(player);
        }
        return entity.getWorld().getTileEntity(entity.getPos()) == entity && player.getDistanceSq((double) entity.getPos().getX() + 0.5D, (double) entity.getPos().getY() + 0.5D, (double) entity.getPos().getZ() + 0.5D) <= 64.0D;
    }
}
