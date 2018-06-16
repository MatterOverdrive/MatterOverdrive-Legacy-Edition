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

import com.google.gson.JsonObject;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

/**
 * Created by Simeon on 3/1/2016.
 */
public class SoundReward implements IQuestReward {
    String soundName;
    float volume;
    float pitch;

    @Override
    public void loadFromJson(JsonObject object) {
        soundName = JsonUtils.getString(object, "name");
        volume = JsonUtils.getFloat(object, "volume", 1);
        pitch = JsonUtils.getFloat(object, "pitch", 1);
    }

    @Override
    public void giveReward(QuestStack questStack, EntityPlayer entityPlayer) {
        if (!SoundEvent.REGISTRY.containsKey(new ResourceLocation(soundName))) {
            return;
        }
        entityPlayer.world.playSound(null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName)), SoundCategory.MUSIC, volume, pitch);
    }

    @Override
    public boolean isVisible(QuestStack questStack) {
        return false;
    }
}
