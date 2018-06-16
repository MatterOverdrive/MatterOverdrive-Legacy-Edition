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
package matteroverdrive.machines.pattern_monitor;

import matteroverdrive.matter_network.components.TaskQueueComponent;
import matteroverdrive.matter_network.events.MatterNetworkEventReplicate;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.util.TimeTracker;
import net.minecraft.util.ITickable;

/**
 * Created by Simeon on 2/6/2016.
 */
public class ComponentTaskProcessingPatternMonitor extends TaskQueueComponent<MatterNetworkTaskReplicatePattern, TileEntityMachinePatternMonitor> implements ITickable {
    public static final int REPLICATION_SEARCH_TIME = 40;
    private final TimeTracker patternSendTimeTracker;

    public ComponentTaskProcessingPatternMonitor(String name, TileEntityMachinePatternMonitor machine, int taskQueueCapacity, int queueId) {
        super(name, machine, taskQueueCapacity, queueId);
        patternSendTimeTracker = new TimeTracker();
    }

    public void addReplicateTask(MatterNetworkTaskReplicatePattern task) {
        if (getTaskQueue().queue(task)) {
            sendTaskQueueAddedToWatchers(task.getId());
        }
    }

    @Override
    public void update() {
        if (!getWorld().isRemote) {
            MatterNetworkTaskReplicatePattern replicatePattern = getTaskQueue().peek();
            if (replicatePattern != null) {
                if (patternSendTimeTracker.hasDelayPassed(getWorld(), REPLICATION_SEARCH_TIME)) {
                    MatterNetworkEventReplicate.Request requestPatternReplication = new MatterNetworkEventReplicate.Request(replicatePattern.getPattern(), replicatePattern.getAmount());
                    machine.getNetwork().post(requestPatternReplication);
                    if (requestPatternReplication.isAccepted()) {
                        getTaskQueue().dequeue();
                    }
                }
            }
        }
    }
}
