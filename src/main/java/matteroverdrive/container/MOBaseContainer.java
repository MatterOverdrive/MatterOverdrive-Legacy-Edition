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
package matteroverdrive.container;

import matteroverdrive.container.slot.MOSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 3/16/2015.
 */
public abstract class MOBaseContainer extends Container {
    public MOBaseContainer() {
    }

    public MOBaseContainer(InventoryPlayer inventoryPlayer) {
    }

    @Nonnull
    public Slot addSlotToContainer(Slot slot) {
        return super.addSlotToContainer(slot);
    }

    public MOSlot getSlotAt(int id) {
        return (MOSlot) inventorySlots.get(id);
    }
}
