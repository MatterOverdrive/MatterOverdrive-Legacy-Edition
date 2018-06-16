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
package matteroverdrive.init;

import matteroverdrive.api.internal.Storage;
import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.data.MatterStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * @author shadowfacts
 */
public class MatterOverdriveCapabilities {

    //	MO
    @CapabilityInject(IMatterHandler.class)
    public static Capability<IMatterHandler> MATTER_HANDLER;

    public static void init() {
        CapabilityManager.INSTANCE.register(IMatterHandler.class, new Storage<>(), () -> new MatterStorage(2000));
    }

}