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
package matteroverdrive.world;

import matteroverdrive.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MOLootTableManager {

    @SubscribeEvent
    public void dungeonLootLoad(LootTableLoadEvent event) {
        if (event.getName().toString().equals("minecraft:chests/simple_dungeon")) {
            LootTableList.register(new ResourceLocation("matteroverdrive:inject/simple_dungeon"));
            LootEntry entry = new LootEntryTable(new ResourceLocation("matteroverdrive:inject/simple_dungeon"), 100, 1, new LootCondition[]{}, "Blue_Pill");

            LootPool pool = new LootPool(new LootEntry[]{entry}, new LootCondition[]{}, new RandomValueRange(0, 10), new RandomValueRange(0, 10), "Blue_Pill");
            //event.getTable().addPool(pool);
        }
    }

    @SubscribeEvent
    public void generateCrashedShipLoot(LootTableLoadEvent event) {
        if (event.getName().toString().equals("matteroverdrive:crashed_ship")) {
            LootTable crashedShipLootTable = event.getTable();

            LootEntry entry = new LootEntryTable(new ResourceLocation("matteroverdrive:crashed_ship"), 100, 1, new LootCondition[]{}, Reference.CHEST_GEN_ANDROID_HOUSE);
        }
    }

}
