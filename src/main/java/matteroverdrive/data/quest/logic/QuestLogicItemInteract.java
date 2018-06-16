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
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestLogicState;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.api.quest.QuestState;
import matteroverdrive.data.quest.QuestItem;
import matteroverdrive.util.MOJsonHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 1/24/2016.
 */
public class QuestLogicItemInteract extends AbstractQuestLogicRandomItem {
    boolean consumeItem;

    public QuestLogicItemInteract() {
    }

    public QuestLogicItemInteract(QuestItem item, boolean consumeItem) {
        this.consumeItem = consumeItem;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject) {
        super.loadFromJson(jsonObject);
        consumeItem = MOJsonHelper.getBool(jsonObject, "consume", false);
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info) {
        info = info.replace("$itemName", getItemName(questStack));
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex) {
        return hasInteracted(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex) {
        objective = objective.replace("$itemName", getItemName(questStack));
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack) {

    }

    @Override
    public QuestLogicState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer) {
        if (event instanceof PlayerInteractEvent.RightClickItem) {
            PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;
            if (!interactEvent.getItemStack().isEmpty()) {

            }
            {
                boolean isSameItem = matches(questStack, ((PlayerInteractEvent.RightClickItem) event).getItemStack());
                if (isSameItem) {
                    setInteracted(questStack, true);
                    if (consumeItem) {
                        interactEvent.getItemStack().shrink(1);
                    }
                    markComplete(questStack, entityPlayer);
                    return new QuestLogicState(QuestState.Type.COMPLETE, true);
                }
            }
        }
        return null;
    }

    @Override
    public void onQuestTaken(QuestStack questStack, EntityPlayer entityPlayer) {

    }

    @Override
    public void onQuestCompleted(QuestStack questStack, EntityPlayer entityPlayer) {

    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards) {

    }

    public boolean hasInteracted(QuestStack questStack) {
        if (questStack.getTagCompound() != null) {
            return questStack.getTagCompound().getBoolean("used");
        }
        return false;
    }

    public void setInteracted(QuestStack questStack, boolean readBook) {
        if (questStack.getTagCompound() == null) {
            questStack.setTagCompound(new NBTTagCompound());
        }

        questStack.getTagCompound().setBoolean("used", readBook);
    }
}
