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
import matteroverdrive.api.exceptions.MOQuestParseException;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestLogicState;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.api.quest.QuestState;
import matteroverdrive.util.MOJsonHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 12/31/2015.
 */
public class QuestLogicSingleEvent extends AbstractQuestLogic {
    Class<? extends Event> event;

    public QuestLogicSingleEvent() {
    }

    public QuestLogicSingleEvent(Class<? extends Event> event) {
        this.event = event;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject) {
        String eventName = MOJsonHelper.getString(jsonObject, "event");
        try {
            event = (Class<? extends Event>) Class.forName(eventName);
        } catch (ClassNotFoundException e) {
            throw new MOQuestParseException(String.format("Could not find event class from type: %s", eventName), e);
        } catch (ClassCastException e) {
            throw new MOQuestParseException(String.format("Class must be derived form Forge Event Super class"), e);
        }
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info) {
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex) {
        return hasEventFired(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex) {
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack) {

    }

    @Override
    public QuestLogicState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer) {
        if (!hasEventFired(questStack) && this.event.isInstance(event)) {
            markComplete(questStack, entityPlayer);
            setEventFired(questStack);
            return new QuestLogicState(QuestState.Type.COMPLETE, true);
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

    public boolean hasEventFired(QuestStack questStack) {
        if (hasTag(questStack)) {
            return getTag(questStack).getBoolean("e");
        }
        return false;
    }

    public void setEventFired(QuestStack questStack) {
        initTag(questStack);
        getTag(questStack).setBoolean("e", true);
    }
}
