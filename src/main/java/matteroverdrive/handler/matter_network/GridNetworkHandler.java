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
package matteroverdrive.handler.matter_network;

import matteroverdrive.api.transport.IGridNetwork;
import matteroverdrive.api.transport.IGridNode;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Simeon on 1/28/2016.
 */
public abstract class GridNetworkHandler<K extends IGridNode, T extends IGridNetwork<K>> {
    public Stack<T> networkPool;
    public Set<T> activeNetworkList;

    public GridNetworkHandler() {
        networkPool = new Stack<>();
        activeNetworkList = new HashSet<>();
    }

    public void recycleNetwork(T network) {
        networkPool.push(network);
        activeNetworkList.remove(network);
    }

    public abstract T createNewNetwork(K node);

    public T getNetwork(K node) {
        T network;
        if (networkPool.isEmpty()) {
            network = createNewNetwork(node);
        } else {
            network = networkPool.pop();
        }

        activeNetworkList.add(network);
        return network;
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload unload) {
        if (!unload.getWorld().isRemote && unload.getWorld().provider.getDimension() == 0) {
            activeNetworkList.forEach(T::recycle);
            activeNetworkList.clear();
        }
    }
}
