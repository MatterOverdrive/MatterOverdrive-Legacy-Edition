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
            event.getTable().addPool(pool);

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
