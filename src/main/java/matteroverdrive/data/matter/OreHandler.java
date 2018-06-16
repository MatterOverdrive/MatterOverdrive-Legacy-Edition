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
package matteroverdrive.data.matter;

import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 1/17/2016.
 */
public class OreHandler extends MatterEntryHandlerAbstract<ItemStack> {
    private final int matter;
    private final boolean isFinalHandler;

    public OreHandler(int matter) {
        this.matter = matter;
        this.isFinalHandler = false;
    }

    public OreHandler(int matter, boolean isFinalHandler) {
        this.matter = matter;
        this.isFinalHandler = isFinalHandler;
    }

    public OreHandler(int matter, boolean isFinalHandler, int priority) {
        this.matter = matter;
        this.isFinalHandler = isFinalHandler;
        this.priority = priority;
    }

    @Override
    public int modifyMatter(ItemStack itemStack, int originalMatter) {
        return matter;
    }

    @Override
    public boolean finalModification(ItemStack itemStack) {
        return isFinalHandler;
    }
}
