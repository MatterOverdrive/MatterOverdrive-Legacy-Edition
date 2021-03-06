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

import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 6/15/2015.
 */
public class ContainerStarMap extends ContainerMachine<TileEntityMachineStarMap> {
    public ContainerStarMap() {
        super();
    }

    public ContainerStarMap(InventoryPlayer inventory, TileEntityMachineStarMap machine) {
        super(inventory, machine);
    }

    @Override
    protected void init(InventoryPlayer inventory) {

        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 270, true, true);
    }
}
