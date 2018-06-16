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
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Simeon on 1/17/2016.
 */
public class ItemStackHandlerCachable extends MatterEntryHandlerAbstract<ItemStack> {
    private boolean metadataAware;
    private boolean isFinalHandler;
    private int meta;
    private int matter;
    private boolean custom;

    public ItemStackHandlerCachable() {

    }

    public ItemStackHandlerCachable(int matter) {
        this.matter = matter;
    }

    public ItemStackHandlerCachable(int matter, boolean isFinalHandler) {
        this.matter = matter;
        this.isFinalHandler = isFinalHandler;
    }

    public ItemStackHandlerCachable(int matter, int meta) {
        this(matter);
        this.meta = (short) meta;
        this.metadataAware = true;
    }

    public ItemStackHandlerCachable(int matter, int meta, boolean isFinalHandler) {
        this(matter, meta);
        this.isFinalHandler = isFinalHandler;
    }

    @Override
    public int modifyMatter(ItemStack itemStack, int originalMatter) {
        if (metadataAware) {
            if (itemStack.getItemDamage() == meta) {
                return matter;
            }
        } else {
            return matter;
        }
        return originalMatter;
    }

    @Override
    public boolean finalModification(ItemStack itemStack) {
        return false;
    }

    public void writeTo(DataOutput output) throws IOException {
        output.writeBoolean(metadataAware);
        output.writeBoolean(isFinalHandler);
        output.writeShort(meta);
        output.writeInt(matter);
        output.writeByte(priority);
    }

    public void writeTo(NBTTagCompound tagCompound) {
        tagCompound.setBoolean("metaAware", metadataAware);
        tagCompound.setBoolean("final", isFinalHandler);
        tagCompound.setShort("meta", (short) meta);
        tagCompound.setInteger("matter", matter);
        tagCompound.setByte("priority", (byte) priority);
    }

    public void readFrom(DataInput dataInput) throws IOException {
        metadataAware = dataInput.readBoolean();
        isFinalHandler = dataInput.readBoolean();
        meta = dataInput.readShort();
        matter = dataInput.readInt();
        priority = dataInput.readByte();
    }

    public void readFrom(NBTTagCompound tagCompound) {
        metadataAware = tagCompound.getBoolean("metaAware");
        isFinalHandler = tagCompound.getBoolean("final");
        meta = tagCompound.getShort("meta");
        matter = tagCompound.getInteger("matter");
        priority = tagCompound.getByte("priority");
    }

    public boolean isCustom() {
        return custom;
    }

    public ItemStackHandlerCachable markCustom() {
        this.custom = true;
        return this;
    }
}
