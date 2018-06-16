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

import matteroverdrive.api.matter.IMatterEntry;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Simeon on 1/18/2016.
 */
public abstract class MatterEntryAbstract<KEY, MAT> implements IMatterEntry<KEY, MAT> {
    protected final List<IMatterEntryHandler<MAT>> handlers;
    protected KEY key;

    public MatterEntryAbstract() {
        this.handlers = new ArrayList<>();
    }

    public MatterEntryAbstract(KEY key) {
        this();
        this.key = key;
    }

    @Override
    public int getMatter(MAT key) {
        int matter = 0;
        for (IMatterEntryHandler handler : handlers) {
            matter = handler.modifyMatter(key, matter);
            if (handler.finalModification(key)) {
                return matter;
            }
        }
        return matter;
    }

    public abstract void writeTo(DataOutput output) throws IOException;

    public abstract void writeTo(NBTTagCompound tagCompound);

    public abstract void readFrom(DataInput input) throws IOException;

    public abstract void readFrom(NBTTagCompound tagCompound);

    public abstract void readKey(String data);

    public abstract String writeKey();

    public abstract boolean hasCached();

    @Override
    public void addHandler(IMatterEntryHandler<MAT> handler) {
        handlers.add(handler);
        Collections.sort(handlers);
    }

    public void clearHandlers() {
        handlers.clear();
    }

    public KEY getKey() {
        return key;
    }
}
