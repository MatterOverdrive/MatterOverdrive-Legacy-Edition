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
package matteroverdrive.data.quest.rewards;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.quest.IQuest;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.entity.player.OverdriveExtendedProperties;
import matteroverdrive.util.MOJsonHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 1/3/2016.
 */
public class QuestStackReward implements IQuestReward {
    QuestStack questStack;
    String questName;
    NBTTagCompound questNbt;
    String[] copyNBT;
    boolean visible;

    public QuestStackReward() {
    }

    public QuestStackReward(QuestStack questStack) {
        this.questStack = questStack;
    }

    public QuestStackReward setCopyNBT(String... copyNBT) {
        this.copyNBT = copyNBT;
        return this;
    }

    @Override
    public void loadFromJson(JsonObject object) {
        questName = MOJsonHelper.getString(object, "id");
        questNbt = MOJsonHelper.getNbt(object, "nbt", null);
        if (object.has("copy_nbt") && object.get("copy_nbt").isJsonArray()) {
            JsonArray array = object.get("copy_nbt").getAsJsonArray();
            String[] elements = new String[array.size()];
            for (int i = 0; i < elements.length; i++) {
                elements[i] = array.get(i).getAsString();
            }
            copyNBT = elements;
        }
        this.visible = MOJsonHelper.getBool(object, "visible", true);
    }

    @Override
    public void giveReward(QuestStack completedQuest, EntityPlayer entityPlayer) {
        QuestStack questStack = getQuestStack();

        if (questStack != null && questStack.canAccept(entityPlayer, questStack)) {
            OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(entityPlayer);
            if (extendedProperties != null) {
                QuestStack questStackCopy = questStack.copy();
                questStackCopy.getQuest().initQuestStack(entityPlayer.getRNG(), questStackCopy);
                if (copyNBT != null && copyNBT.length > 0 && completedQuest.getTagCompound() != null) {
                    if (questStackCopy.getTagCompound() == null) {
                        questStackCopy.setTagCompound(new NBTTagCompound());
                    }

                    for (String aCopyNBT : copyNBT) {
                        NBTBase nbtBase = completedQuest.getTagCompound().getTag(aCopyNBT);
                        if (nbtBase != null) {

                            questStackCopy.getTagCompound().setTag(aCopyNBT, nbtBase.copy());
                        }
                    }
                }
                extendedProperties.addQuest(questStackCopy);
            }
        }
    }

    @Override
    public boolean isVisible(QuestStack questStack) {
        return visible;
    }

    public QuestStack getQuestStack() {
        if (questStack == null) {
            IQuest quest = MatterOverdrive.QUESTS.getQuestByName(questName);
            if (quest != null) {
                QuestStack questStack = new QuestStack(quest);
                if (questNbt != null) {
                    questStack.setTagCompound(questNbt);
                }
                return questStack;
            }
        } else {
            return questStack;
        }
        return null;
    }
}
