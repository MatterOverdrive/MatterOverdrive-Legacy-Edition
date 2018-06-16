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
package matteroverdrive.container.matter_network;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.network.packet.client.task_queue.PacketSyncTaskQueue;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 2/6/2016.
 */
public class ContainerTaskQueueMachine<T extends MOTileEntityMachine & IMatterNetworkDispatcher> extends ContainerMachine<T> implements ITaskQueueWatcher {
    public ContainerTaskQueueMachine(InventoryPlayer inventory, T machine) {
        super(inventory, machine);
    }

    @Override
    public void onWatcherAdded(MOTileEntityMachine machine) {
        super.onWatcherAdded(machine);
        if (machine instanceof IMatterNetworkDispatcher) {
            sendAllTaskQueues((IMatterNetworkDispatcher) machine);
        }
    }

    private void sendAllTaskQueues(IMatterNetworkDispatcher dispatcher) {
        for (int i = 0; i < dispatcher.getTaskQueueCount(); i++) {
            sendTaskQueue(dispatcher, i);
        }
    }

    private void sendTaskQueue(IMatterNetworkDispatcher dispatcher, int queueId) {
        MatterOverdrive.NETWORK.sendTo(new PacketSyncTaskQueue(dispatcher, queueId), (EntityPlayerMP) getPlayer());
    }

    @Override
    public void onTaskAdded(IMatterNetworkDispatcher dispatcher, long taskId, int queueId) {
        sendTaskQueue(dispatcher, queueId);
    }

    @Override
    public void onTaskRemoved(IMatterNetworkDispatcher dispatcher, long taskId, int queueId) {
        sendTaskQueue(dispatcher, queueId);
    }

    @Override
    public void onTaskChanged(IMatterNetworkDispatcher dispatcher, long taskId, int queueId) {
        sendTaskQueue(dispatcher, queueId);
    }
}
