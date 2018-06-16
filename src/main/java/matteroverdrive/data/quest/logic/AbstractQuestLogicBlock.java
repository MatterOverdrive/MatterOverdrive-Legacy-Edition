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
package matteroverdrive.data.quest.logic;

import com.google.gson.JsonObject;
import matteroverdrive.data.quest.QuestBlock;
import matteroverdrive.data.quest.QuestItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 1/5/2016.
 */
public abstract class AbstractQuestLogicBlock extends AbstractQuestLogic {
    protected QuestBlock block;
    protected QuestItem blockStack;

    public AbstractQuestLogicBlock() {
    }

    public AbstractQuestLogicBlock(QuestBlock block) {
        this.block = block;
    }

    public AbstractQuestLogicBlock(QuestItem blockStack) {
        this.blockStack = blockStack;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject) {
        if (jsonObject.has("block")) {
            block = new QuestBlock(jsonObject.getAsJsonObject("block"));
        } else {
            blockStack = new QuestItem(jsonObject.getAsJsonObject("item"));
        }
    }

    protected boolean areBlockStackTheSame(ItemStack stack) {
        return blockStack.getItemStack().isItemEqual(stack) && ItemStack.areItemStackTagsEqual(blockStack.getItemStack(), stack);
    }

    protected boolean areBlocksTheSame(IBlockState blockState) {
        return this.block.isTheSame(blockState);
    }

    protected String replaceBlockNameInText(String text) {
        if (blockStack != null) {
            text = text.replace("$block", blockStack.getItemStack().getDisplayName());
        } else {
            text = text.replace("$block", block.getBlockState().getBlock().getLocalizedName());
        }
        return text;
    }
}
