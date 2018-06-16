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

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Simeon on 1/30/2016.
 */
public class ItemPatternMapping {
    private ItemPattern itemPattern;
    private BlockPos databaseId;
    private int storageId;
    private int patternId;

    public ItemPatternMapping(ByteBuf byteBuf) {
        itemPattern = ItemPattern.fromBuffer(byteBuf);
        databaseId = BlockPos.fromLong(byteBuf.readLong());
        storageId = byteBuf.readByte();
        patternId = byteBuf.readByte();
    }

    public ItemPatternMapping(ItemPattern itemPattern, BlockPos databaseId, int storageId, int patternId) {
        this.databaseId = databaseId;
        this.itemPattern = itemPattern;
        this.storageId = storageId;
        this.patternId = patternId;
    }

    public ItemPattern getItemPattern() {
        return itemPattern;
    }

    public BlockPos getDatabaseId() {
        return databaseId;
    }

    public void writeToBuffer(ByteBuf byteBuf) {
        ItemPattern.writeToBuffer(byteBuf, itemPattern);
        byteBuf.writeLong(databaseId.toLong());
        byteBuf.writeByte(storageId);
        byteBuf.writeByte(patternId);
    }

    public int getStorageId() {
        return storageId;
    }

    public int getPatternId() {
        return patternId;
    }
}
