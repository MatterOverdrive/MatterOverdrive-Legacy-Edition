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
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOQuestHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Constructor;

/**
 * Created by Simeon on 2/13/2016.
 */
public class EntityReward implements IQuestReward {
    private String entityId;
    private int count;
    private boolean positionFromNbt;
    private Vec3d positionOffset;
    private NBTTagCompound nbtTagCompound;

    public EntityReward() {
    }

    public EntityReward(String entityId, int count) {
        this.entityId = entityId;
        this.count = count;
    }

    @Override
    public void loadFromJson(JsonObject object) {
        entityId = MOJsonHelper.getString(object, "id");
        if (object.has("position")) {
            String positionType = MOJsonHelper.getString(object, "position");
            if (positionType.equalsIgnoreCase("nbt")) {
                positionFromNbt = true;
            }
        }
        count = MOJsonHelper.getInt(object, "count");
        nbtTagCompound = MOJsonHelper.getNbt(object, "nbt", null);
        positionOffset = MOJsonHelper.getVec3(object, "offset", new Vec3d(0, 0, 0));
    }

    @Override
    public void giveReward(QuestStack questStack, EntityPlayer entityPlayer) {
        for (ResourceLocation key : EntityList.getEntityNameList()) {
            MOLog.info(key.toString());
        }
        Class<? extends Entity> entityClass = EntityList.getClass(new ResourceLocation(entityId));
        EntityRegistry.EntityRegistration entityRegistration = EntityRegistry.instance().lookupModSpawn(entityClass, true);
        if (entityRegistration != null) {
            for (int i = 0; i < count; i++) {
                try {
                    Constructor<? extends Entity> constructor = entityRegistration.getEntityClass().getConstructor(World.class);
                    Entity entity = constructor.newInstance(entityPlayer.world);
                    if (positionFromNbt) {
                        BlockPos pos = MOQuestHelper.getPosition(questStack);
                        if (pos != null) {
                            entity.setPosition(pos.getX() + positionOffset.x, pos.getY() + positionOffset.y, pos.getZ() + positionOffset.z);
                        } else {
                            entity.setPosition(entityPlayer.posX + positionOffset.x, entityPlayer.posY + positionOffset.y, entityPlayer.posZ + positionOffset.z);
                        }
                    } else {
                        entity.setPosition(entityPlayer.posX + positionOffset.x, entityPlayer.posY + positionOffset.y, entityPlayer.posZ + positionOffset.z);
                    }
                    if (nbtTagCompound != null) {
                        entity.readFromNBT(nbtTagCompound);
                    }
                    entityPlayer.world.spawnEntity(entity);
                } catch (Exception e) {
                    MOLog.log(Level.WARN, e, "Could not spawn Entity reward of type %s for quest %s", entityRegistration.getEntityClass(), questStack.getTitle());
                }
            }
        } else {
            MOLog.warn("Could not find an entity of type %s while giving an entity reward for quest %s", entityId, questStack.getTitle());
        }
    }

    @Override
    public boolean isVisible(QuestStack questStack) {
        return false;
    }


    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isPositionFromNbt() {
        return positionFromNbt;
    }

    public void setPositionFromNbt(boolean positionFromNbt) {
        this.positionFromNbt = positionFromNbt;
    }

    public Vec3d getPositionOffset() {
        return positionOffset;
    }

    public void setPositionOffset(Vec3d positionOffset) {
        this.positionOffset = positionOffset;
    }

    public NBTTagCompound getNbtTagCompound() {
        return nbtTagCompound;
    }

    public void setNbtTagCompound(NBTTagCompound nbtTagCompound) {
        this.nbtTagCompound = nbtTagCompound;
    }

}
