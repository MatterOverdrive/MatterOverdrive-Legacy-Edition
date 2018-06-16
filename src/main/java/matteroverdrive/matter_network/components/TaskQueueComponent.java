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
package matteroverdrive.matter_network.components;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.container.matter_network.ITaskQueueWatcher;
import matteroverdrive.data.Inventory;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineComponentAbstract;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import net.minecraft.nbt.NBTTagCompound;

import java.util.EnumSet;

/**
 * Created by Simeon on 2/6/2016.
 */
public class TaskQueueComponent<T extends MatterNetworkTask, M extends MOTileEntityMachine & IMatterNetworkDispatcher> extends MachineComponentAbstract<M> {
    private final int queueId;
    private MatterNetworkTaskQueue<T> taskQueue;

    public TaskQueueComponent(String name, M machine, int taskQueueCapacity, int queueId) {
        super(machine);
        taskQueue = new MatterNetworkTaskQueue<>(name, taskQueueCapacity);
        this.queueId = queueId;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.DATA)) {
            if (nbt.hasKey("tasks")) {
                taskQueue.readFromNBT(nbt.getCompoundTag("tasks"));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.DATA) && toDisk) {
            NBTTagCompound taskQueueTag = new NBTTagCompound();
            taskQueue.writeToNBT(taskQueueTag);
            nbt.setTag("tasks", taskQueueTag);
        }
    }

    @Override
    public void registerSlots(Inventory inventory) {

    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void onMachineEvent(MachineEvent event) {

    }

    public void sendTaskQueueAddedToWatchers(long taskId) {
        machine.getWatchers().stream().filter(watcher -> watcher instanceof ITaskQueueWatcher).forEach(watcher -> ((ITaskQueueWatcher) watcher).onTaskAdded(machine, taskId, queueId));
    }

    public void sendTaskQueueRemovedFromWatchers(long taskId) {
        machine.getWatchers().stream().filter(watcher -> watcher instanceof ITaskQueueWatcher).forEach(watcher -> ((ITaskQueueWatcher) watcher).onTaskRemoved(machine, taskId, queueId));
    }

    //region Getters and Setters
    public MatterNetworkTaskQueue<T> getTaskQueue() {
        return taskQueue;
    }

    public int getQueueId() {
        return queueId;
    }
    //endregion
}
