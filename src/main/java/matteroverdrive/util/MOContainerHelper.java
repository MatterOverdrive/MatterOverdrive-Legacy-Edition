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

import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.container.slot.SlotPlayerInventory;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 3/16/2015.
 */
public class MOContainerHelper {
    public static void AddPlayerSlots(InventoryPlayer inventory, MOBaseContainer container, int x, int y, boolean main, boolean hotbar) {
        if (main) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    container.addSlotToContainer(new SlotPlayerInventory(inventory, j + i * 9 + 9, x + j * 18, y + i * 18, false));
                }
            }
        }

        if (hotbar) {
            for (int i = 0; i < 9; i++) {
                container.addSlotToContainer(new SlotPlayerInventory(inventory, i, x + i * 18, y + 61, true));
            }
        }
    }
}
