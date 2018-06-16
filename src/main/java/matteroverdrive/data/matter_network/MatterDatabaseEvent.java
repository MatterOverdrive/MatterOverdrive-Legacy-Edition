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

import matteroverdrive.api.matter.IMatterDatabase;

/**
 * Created by Simeon on 1/30/2016.
 */
public class MatterDatabaseEvent {
    public final IMatterDatabase database;

    public MatterDatabaseEvent(IMatterDatabase database) {
        this.database = database;
    }

    public static class Removed extends MatterDatabaseEvent {
        public Removed(IMatterDatabase database) {
            super(database);
        }
    }

    public static class Added extends MatterDatabaseEvent {
        public Added(IMatterDatabase database) {
            super(database);
        }
    }

    public static class PatternStorageChanged extends MatterDatabaseEvent implements IMatterNetworkEvent {
        public final int storageID;

        public PatternStorageChanged(IMatterDatabase database, int storageID) {
            super(database);
            this.storageID = storageID;
        }
    }

    public static class PatternChanged extends MatterDatabaseEvent implements IMatterNetworkEvent {
        public int patternStorageId;
        public int patternId;

        public PatternChanged(IMatterDatabase database, int patternStorageId, int patternId) {
            super(database);
            this.patternStorageId = patternStorageId;
            this.patternId = patternId;
        }
    }
}
