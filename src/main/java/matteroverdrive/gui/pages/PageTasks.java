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
package matteroverdrive.gui.pages;

import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementTaskList;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;

/**
 * Created by Simeon on 4/21/2015.
 */
public class PageTasks extends ElementBaseGroup {
    ElementTaskList taskList;

    public PageTasks(MOGuiBase gui, int posX, int posY, MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue) {
        this(gui, posX, posY, 0, 0, taskQueue);
    }

    public PageTasks(MOGuiBase gui, int posX, int posY, int width, int height, MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue) {
        super(gui, posX, posY, width, height);
        taskList = new ElementTaskList(gui, gui, 48, 36, 150, 120, taskQueue);
    }

    @Override
    public void init() {
        super.init();
        addElement(taskList);
    }
}
