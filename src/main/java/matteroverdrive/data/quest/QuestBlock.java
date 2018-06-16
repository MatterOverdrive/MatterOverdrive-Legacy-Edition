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
package matteroverdrive.data.quest;

import com.google.gson.JsonObject;
import matteroverdrive.util.MOJsonHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.Loader;

/**
 * Created by Simeon on 12/24/2015.
 */
public class QuestBlock {
    IBlockState block;
    String blockName;
    int blockMeta;
    boolean hasMeta;
    String mod;

    public QuestBlock(JsonObject object) {
        if (object.has("meta")) {
            setBlockMeta(MOJsonHelper.getInt(object, "meta"));
        }
        blockName = MOJsonHelper.getString(object, "id");
        mod = MOJsonHelper.getString(object, "mod", null);
    }

    public QuestBlock(IBlockState block) {
        this.block = block;
    }

    public QuestBlock(String blockName, String mod) {
        this.blockName = blockName;
        this.mod = mod;
    }

    public static QuestBlock fromBlock(IBlockState block) {
        return new QuestBlock(block);
    }

    public boolean isModded() {
        return mod != null && !mod.isEmpty();
    }

    public boolean isModPresent() {
        return Loader.isModLoaded(mod);
    }

    public boolean canBlockExist() {
        if (isModded()) {
            return isModPresent();
        }
        return true;
    }

    public IBlockState getBlockState() {
        if (isModded() || block == null) {
            if (hasMeta) {
                return Block.getBlockFromName(blockName).getStateFromMeta(blockMeta);
            }
            return Block.getBlockFromName(blockName).getDefaultState();

        } else {
            return block;
        }
    }

    public boolean isTheSame(IBlockState blockState) {
        if (hasMeta) {
            return getBlockState().equals(blockState);
        } else {
            return getBlockState().getBlock().equals(blockState.getBlock());
        }
    }

    public void setBlockMeta(int meta) {
        this.blockMeta = meta;
        this.hasMeta = true;
    }
}
