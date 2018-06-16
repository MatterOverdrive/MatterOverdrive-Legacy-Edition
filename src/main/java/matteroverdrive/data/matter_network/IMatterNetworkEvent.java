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
package matteroverdrive.data.matter_network;

import matteroverdrive.api.matter_network.IMatterNetworkClient;
import matteroverdrive.api.network.MatterNetworkTask;

/**
 * Created by Simeon on 1/29/2016.
 */
public interface IMatterNetworkEvent {
    class ClientAdded implements IMatterNetworkEvent {
        public final IMatterNetworkClient client;

        public ClientAdded(IMatterNetworkClient gridNode) {
            this.client = gridNode;
        }
    }

    class ClientRemoved implements IMatterNetworkEvent {
        public final IMatterNetworkClient client;

        public ClientRemoved(IMatterNetworkClient gridNode) {
            this.client = gridNode;
        }
    }

    class AddedToNetwork implements IMatterNetworkEvent {
    }

    class RemovedFromNetwork implements IMatterNetworkEvent {
    }

    class Task implements IMatterNetworkEvent {
        public final MatterNetworkTask task;

        public Task(MatterNetworkTask task) {
            this.task = task;
        }
    }
}
