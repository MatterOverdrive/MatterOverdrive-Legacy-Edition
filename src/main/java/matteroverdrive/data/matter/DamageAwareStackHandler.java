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
public class DamageAwareStackHandler extends MatterEntryHandlerAbstract<ItemStack> {
    private final int damage;
    private final int matter;
    private final boolean finalHandle;

    public DamageAwareStackHandler(int damage, int matter) {
        this.damage = damage;
        this.matter = matter;
        this.finalHandle = false;
    }

    public DamageAwareStackHandler(int damage, int matter, boolean finalHandle) {
        this.damage = damage;
        this.matter = matter;
        this.finalHandle = finalHandle;
    }

    public DamageAwareStackHandler(int damage, int matter, boolean finalHandle, int priority) {
        this.priority = priority;
        this.damage = damage;
        this.matter = matter;
        this.finalHandle = finalHandle;
    }

    @Override
    public int modifyMatter(ItemStack itemStack, int originalMatter) {
        if (itemStack.getItemDamage() == damage) {
            return matter;
        }
        return originalMatter;
    }

    @Override
    public boolean finalModification(ItemStack itemStack) {
        if (itemStack.getItemDamage() == damage) {
            return finalHandle;
        }
        return false;
    }
}
