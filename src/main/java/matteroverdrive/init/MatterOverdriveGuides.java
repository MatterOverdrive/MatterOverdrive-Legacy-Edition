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
package matteroverdrive.init;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.guide.*;
import matteroverdrive.guide.infograms.InfogramCreates;
import matteroverdrive.guide.infograms.InfogramDepth;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 8/29/2015.
 */
@SideOnly(Side.CLIENT)
public class MatterOverdriveGuides {
    public static GuideCategory androidCategory;
    public static GuideCategory weaponsCategory;
    public static GuideCategory generalCategory;

    public static void registerGuideElements(FMLInitializationEvent event) {
        MatterOverdriveGuide.registerGuideElementHandler("text", GuideElementText.class);
        MatterOverdriveGuide.registerGuideElementHandler("depth", InfogramDepth.class);
        MatterOverdriveGuide.registerGuideElementHandler("creates", InfogramCreates.class);
        MatterOverdriveGuide.registerGuideElementHandler("recipe", GuideElementRecipe.class);
        MatterOverdriveGuide.registerGuideElementHandler("title", GuideElementTitle.class);
        MatterOverdriveGuide.registerGuideElementHandler("image", GuideElementImage.class);
        MatterOverdriveGuide.registerGuideElementHandler("preview", GuideElementPreview.class);
        MatterOverdriveGuide.registerGuideElementHandler("details", GuideElementDetails.class);
        MatterOverdriveGuide.registerGuideElementHandler("tooltip", GuideElementTooltip.class);
    }

    public static void registerGuides(FMLPostInitializationEvent event) {
        generalCategory = new GuideCategory("general").setHoloIcon("home_icon");
        MatterOverdriveGuide.registerCategory(generalCategory);
        weaponsCategory = new GuideCategory("weapons").setHoloIcon("ammo");
        MatterOverdriveGuide.registerCategory(weaponsCategory);
        androidCategory = new GuideCategory("android").setHoloIcon("android_slot_arms");
        MatterOverdriveGuide.registerCategory(androidCategory);


        //Ore
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.dilithium_ore).setGroup("resources"), 3, 0);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.tritaniumOre).setGroup("resources"), 4, 0);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.dilithium_crystal).setGroup("resources"), 3, 1);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.tritanium_ingot).setGroup("resources"), 4, 1);
        //Machines
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.replicator).setGroup("machines"), 0, 0);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.decomposer).setGroup("machines"), 1, 0);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.recycler).setGroup("machines"), 0, 1);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.matter_analyzer).setGroup("machines"), 1, 1);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.pattern_storage).setGroup("machines"), 0, 2);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.pattern_monitor).setGroup("machines"), 1, 2);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.transporter).setGroup("machines"), 0, 3);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.holoSign).setGroup("machines"), 1, 3);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.inscriber).setGroup("machines"), 0, 4);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.contractMarket).setGroup("machines"), 1, 4);
        //Power
        addEntry(generalCategory, new MOGuideEntry("fusion_reactor").setStackIcons(new ItemStack(MatterOverdrive.BLOCKS.fusion_reactor_controller),
                new ItemStack(MatterOverdrive.BLOCKS.fusion_reactor_coil),
                //new ItemStack(MatterOverdrive.BLOCKS.forceGlass),
                new ItemStack(MatterOverdrive.BLOCKS.fusionReactorIO)).setGroup("power"), 3, 3);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.gravitational_anomaly).setGroup("power"), 4, 3);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.pylon).setGroup("power"), 3, 4);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.solar_panel).setGroup("power"), 4, 4);
        addEntry(generalCategory, new MOGuideEntry("batteries").setStackIcons(new ItemStack(MatterOverdrive.ITEMS.battery), new ItemStack(MatterOverdrive.ITEMS.hc_battery), new ItemStack(MatterOverdrive.ITEMS.creative_battery)).setGroup("power"), 3, 5);
        //Matter
        addEntry(generalCategory, new MOGuideEntry("matter_transport").setStackIcons(MatterOverdrive.BLOCKS.heavy_matter_pipe).setGroup("matter"), 6, 0);
        addEntry(generalCategory, new MOGuideEntry("matter_fail").setStackIcons(MatterOverdrive.ITEMS.matter_dust).setGroup("matter"), 7, 0);
        addEntry(generalCategory, new MOGuideEntry("matter_plasma", MatterOverdrive.ITEMS.matterContainer.getFullStack()).setGroup("matter"), 6, 1);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.matter_scanner).setGroup("matter"), 7, 1);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.pattern_drive).setGroup("matter"), 6, 2);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.portableDecomposer).setGroup("matter"), 7, 2);
        //Matter Network
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.network_pipe).setGroup("matter_network"), 6, 4);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.network_switch).setGroup("matter_network"), 7, 4);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.networkFlashDrive).setGroup("matter_network"), 6, 5);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.network_router).setGroup("matter_network"), 7, 5);

        //Items
        int itemsY = 7;
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.spacetime_equalizer).setGroup("items"), 0, itemsY);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.security_protocol).setGroup("items"), 1, itemsY);
        addEntry(generalCategory, new MOGuideEntry("upgrades").setStackIcons(MatterOverdrive.ITEMS.item_upgrade).setGroup("items"), 2, itemsY);
        addEntry(generalCategory, new MOGuideEntry("drinks").setStackIcons(new ItemStack(MatterOverdrive.ITEMS.romulan_ale), new ItemStack(MatterOverdrive.ITEMS.earl_gray_tea)).setGroup("items"), 3, itemsY);
        addEntry(generalCategory, new MOGuideEntry("food").setStackIcons(new ItemStack(MatterOverdrive.ITEMS.emergency_ration)).setGroup("items"), 4, itemsY);
        itemsY++;
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.wrench).setGroup("items"), 0, itemsY);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.transportFlashDrive).setGroup("items"), 1, itemsY);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.contract).setGroup("items"), 2, itemsY);

        //Weapons
        addEntry(weaponsCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.phaser).setGroup("weapons"), 4, 0);
        addEntry(weaponsCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.phaserRifle).setGroup("weapons"), 5, 0);
        addEntry(weaponsCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.omniTool).setGroup("weapons"), 6, 0);
        addEntry(weaponsCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.plasmaShotgun).setGroup("weapons"), 4, 1);
        addEntry(weaponsCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.ionSniper).setGroup("weapons"), 5, 1);
        addEntry(weaponsCategory, new MOGuideEntry("tritanium_tools").setStackIcons(new ItemStack(MatterOverdrive.ITEMS.tritaniumAxe), new ItemStack(MatterOverdrive.ITEMS.tritaniumSword), new ItemStack(MatterOverdrive.ITEMS.tritaniumHoe), new ItemStack(MatterOverdrive.ITEMS.tritaniumPickaxe)).setGroup("weapons"), 6, 1);
        //Parts
        addEntry(weaponsCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.energyPack).setGroup("parts"), 1, 0);
        addEntry(weaponsCategory, new MOGuideEntry("weapon.modules.barrels").setStackIcons(MatterOverdrive.ITEMS.weapon_module_barrel).setGroup("parts"), 2, 0);
        addEntry(weaponsCategory, new MOGuideEntry("weapon.modules.colors").setStackIcons(MatterOverdrive.ITEMS.weapon_module_color).setGroup("parts"), 1, 1);
        addEntry(weaponsCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.sniperScope).setGroup("parts"), 2, 1);
        //Armor
        addEntry(weaponsCategory, new MOGuideEntry("tritanium_armor").setStackIcons(new ItemStack(MatterOverdrive.ITEMS.tritaniumChestplate), new ItemStack(MatterOverdrive.ITEMS.tritaniumLeggings), new ItemStack(MatterOverdrive.ITEMS.tritaniumBoots), new ItemStack(MatterOverdrive.ITEMS.tritaniumHelmet)).setGroup("armor"), 1, 3);
        //Machines
        addEntry(weaponsCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.weapon_station).setGroup("machines"), 4, 3);


        //Items
        addEntry(androidCategory, new MOGuideEntry("android.pills").setStackIcons(MatterOverdrive.ITEMS.androidPill).setGroup("items"), 5, 1);
        addEntry(androidCategory, new MOGuideEntry("android.parts").setStackIcons(MatterOverdrive.ITEMS.androidParts).setGroup("items"), 5, 2);
        addEntry(androidCategory, new MOGuideEntryItem(MatterOverdrive.ITEMS.tritaniumSpine).setGroup("items"), 5, 3);
        //Machines
        addEntry(androidCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.androidStation).setGroup("machines"), 2, 2);
        addEntry(androidCategory, new MOGuideEntryBlock(MatterOverdrive.BLOCKS.chargingStation).setGroup("machines"), 3, 2);


    }

    private static void addEntry(GuideCategory category, MOGuideEntry entry, int x, int y) {
        int paddingTop = 16;
        int paddingLeft = 18;
        category.addEntry(entry);
        entry.setGuiPos(paddingLeft + x * 28, paddingTop + y * 28);
        entry.setId(MatterOverdriveGuide.getNextFreeID());
        MatterOverdriveGuide.registerEntry(entry);
    }
}
