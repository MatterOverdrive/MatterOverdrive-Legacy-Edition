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
package matteroverdrive.network.packet.bi;

import io.netty.buffer.ByteBuf;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Simeon on 6/12/2015.
 */
public class PacketMatterScannerGetPattern extends TileEntityUpdatePacket {
    int id;
    short damage;
    short scannerSlot;
    short type;

    public PacketMatterScannerGetPattern() {
        super();
    }

    public PacketMatterScannerGetPattern(BlockPos pos) {
        super(pos);
    }

    public PacketMatterScannerGetPattern(BlockPos pos, int id, short damage, short scannerSlot, short type) {
        this(pos);
        this.id = id;
        this.damage = damage;
        this.scannerSlot = scannerSlot;
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        id = buf.readInt();
        damage = buf.readShort();
        scannerSlot = buf.readShort();
        type = buf.readShort();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(id);
        buf.writeShort(damage);
        buf.writeShort(scannerSlot);
        buf.writeShort(type);
    }
}
