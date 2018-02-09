/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
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
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.registries.GameData;
import org.apache.commons.io.IOUtils;
import matteroverdrive.data.recipes.EnergyPackRecipe;
import matteroverdrive.data.recipes.InscriberRecipeManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 8/29/2015.
 */
public class MatterOverdriveRecipes {
    public static final List<IRecipe> recipes = new ArrayList<>();

    public static final InscriberRecipeManager INSCRIBER = new InscriberRecipeManager();

    public static void registerBlockRecipes(FMLInitializationEvent event) {
        addShapedRecipe(MatterOverdrive.BLOCKS.decomposer, "TCT", "S S", "NTM", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), 'M', MatterOverdrive.ITEMS.me_conversion_matrix, 'N', MatterOverdrive.ITEMS.integration_matrix, 'S', Blocks.STICKY_PISTON, 'T', MatterOverdrive.ITEMS.tritanium_plate);
        addShapedOreRecipe(MatterOverdrive.BLOCKS.replicator, "PCF", "IHI", "NTM", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), 'M', MatterOverdrive.ITEMS.me_conversion_matrix, 'H', MatterOverdrive.ITEMS.h_compensator, 'I', "ingotIron", 'N', MatterOverdrive.ITEMS.integration_matrix, 'T', MatterOverdrive.ITEMS.tritanium_plate, 'F', MatterOverdrive.ITEMS.networkFlashDrive, 'P', MatterOverdrive.ITEMS.pattern_drive);
        addShapedOreRecipe(MatterOverdrive.BLOCKS.network_router, "IGI", "DFC", "OMO", 'M', MatterOverdrive.ITEMS.machine_casing, 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0), 'I', "ingotIron", 'G', "blockGlass", 'D', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), 'F', MatterOverdrive.ITEMS.networkFlashDrive);
        addShapedOreRecipe(MatterOverdrive.BLOCKS.network_switch, " G ", "CFC", "OMO", 'M', MatterOverdrive.ITEMS.machine_casing, 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0), 'G', "blockGlass", 'F', MatterOverdrive.ITEMS.networkFlashDrive);
        addShapedOreRecipe(new ItemStack(MatterOverdrive.BLOCKS.matter_pipe, 8), " G ", "IMI", " G ", 'M', MatterOverdrive.ITEMS.s_magnet, 'G', "blockGlass", 'I', "ingotIron");
        addShapedOreRecipe(new ItemStack(MatterOverdrive.BLOCKS.network_pipe, 16), "IGI", "BCB", "IGI", 'M', MatterOverdrive.ITEMS.s_magnet, 'G', "blockGlass", 'I', "ingotIron", 'B', "ingotGold", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0));
        addShapedOreRecipe(MatterOverdrive.BLOCKS.matter_analyzer, " C ", "PMF", "ONO", 'O', "blockIron", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), 'M', MatterOverdrive.ITEMS.me_conversion_matrix, 'N', MatterOverdrive.ITEMS.integration_matrix, 'P', MatterOverdrive.ITEMS.pattern_drive, 'F', MatterOverdrive.ITEMS.networkFlashDrive);
        addShapedOreRecipe(MatterOverdrive.BLOCKS.tritanium_block, "TTT", "TTT", "TTT", 'T', "ingotTritanium");
        addShapedRecipe(MatterOverdrive.BLOCKS.machine_hull, " T ", "T T", " T ", 'T', MatterOverdrive.ITEMS.tritanium_plate);
        addShapedOreRecipe(MatterOverdrive.BLOCKS.solar_panel, "CGC", "GQG", "KMK", 'C', Items.COAL, 'Q', "blockQuartz", 'K', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), 'M', MatterOverdrive.ITEMS.machine_casing, 'G', "blockGlass");
        addShapedOreRecipe(MatterOverdrive.BLOCKS.weapon_station, "   ", "GFR", "CMB", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), 'B', MatterOverdrive.ITEMS.battery, 'G', "dustGlowstone", 'R', "dustRedstone", 'M', MatterOverdrive.ITEMS.machine_casing, 'F', MatterOverdrive.ITEMS.forceFieldEmitter);
        addShapedOreRecipe(MatterOverdrive.BLOCKS.pattern_storage, "B3B", "TCT", "2M1", 'B', new ItemStack(Blocks.WOOL, 1, 15), '1', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0), '2', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), '3', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), 'C', Blocks.CHEST, 'M', MatterOverdrive.ITEMS.machine_casing, 'T', "ingotTritanium");
        addShapedRecipe(MatterOverdrive.BLOCKS.pattern_monitor, " H ", "1N1", " F ", '1', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), 'H', MatterOverdrive.BLOCKS.holoSign, 'N', MatterOverdrive.BLOCKS.network_switch, 'F', MatterOverdrive.ITEMS.networkFlashDrive);
        addShapedOreRecipe(MatterOverdrive.BLOCKS.transporter, "TGT", "CMC", "NBH", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), 'M', MatterOverdrive.ITEMS.me_conversion_matrix, 'H', MatterOverdrive.ITEMS.h_compensator, 'E', "enderpearl", 'N', MatterOverdrive.ITEMS.integration_matrix, 'T', MatterOverdrive.ITEMS.tritanium_plate, 'G', "glowstone", 'B', MatterOverdrive.ITEMS.hc_battery);
        addShapedRecipe(MatterOverdrive.BLOCKS.fusion_reactor_coil, "TMT", "M M", "CMC", 'M', MatterOverdrive.ITEMS.s_magnet, 'T', MatterOverdrive.ITEMS.tritanium_plate, 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0));
        addShapedRecipe(MatterOverdrive.BLOCKS.recycler, "T T", "1P2", "NTM", '2', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), '1', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0), 'M', MatterOverdrive.ITEMS.me_conversion_matrix, 'N', MatterOverdrive.ITEMS.integration_matrix, 'T', MatterOverdrive.ITEMS.tritanium_plate, 'P', Blocks.PISTON);
        addShapedRecipe(MatterOverdrive.BLOCKS.gravitational_stabilizer, " H ", "TST", "CMC", 'M', MatterOverdrive.ITEMS.machine_casing, 'S', MatterOverdrive.ITEMS.spacetime_equalizer, 'T', MatterOverdrive.ITEMS.tritanium_plate, 'C', MatterOverdrive.ITEMS.s_magnet, 'H', MatterOverdrive.BLOCKS.holoSign);
        addShapedRecipe(MatterOverdrive.BLOCKS.fusion_reactor_controller, "CHC", "2M3", "CTC", 'C', MatterOverdrive.BLOCKS.fusion_reactor_coil, '2', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), '3', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), 'M', MatterOverdrive.ITEMS.machine_casing, 'T', MatterOverdrive.ITEMS.tritanium_plate, 'H', MatterOverdrive.BLOCKS.holoSign);
        addShapedOreRecipe(MatterOverdrive.BLOCKS.androidStation, "THA", "2F3", "GMR", '3', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), '2', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), 'F', MatterOverdrive.ITEMS.forceFieldEmitter, 'G', "dustGlowstone", 'R', "dustRedstone", 'M', MatterOverdrive.ITEMS.machine_casing, 'H', new ItemStack(MatterOverdrive.ITEMS.androidParts, 1, 0), 'T', new ItemStack(MatterOverdrive.ITEMS.androidParts, 1, 3), 'A', new ItemStack(MatterOverdrive.ITEMS.androidParts, 1, 1));
        addShapedOreRecipe(MatterOverdrive.BLOCKS.starMap, " S ", "CFC", "GMR", 'S', MatterOverdrive.ITEMS.security_protocol, 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), 'M', MatterOverdrive.ITEMS.machine_casing, 'F', MatterOverdrive.ITEMS.forceFieldEmitter, 'G', "dustGlowstone", 'R', "dustRedstone");
        addShapedOreRecipe(MatterOverdrive.BLOCKS.chargingStation, " F ", "EDR", "BMB", 'M', MatterOverdrive.ITEMS.machine_casing, 'B', MatterOverdrive.ITEMS.hc_battery, 'E', Items.ENDER_EYE, 'R', Items.REPEATER, 'F', MatterOverdrive.ITEMS.forceFieldEmitter, 'D', "gemDilithium");
        addShapedOreRecipe(new ItemStack(MatterOverdrive.BLOCKS.heavy_matter_pipe, 8), "RMR", "TMT", "RMR", 'M', MatterOverdrive.ITEMS.s_magnet, 'G', "blockGlass", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'R', "dustRedstone");
        addShapedOreRecipe(MatterOverdrive.BLOCKS.holoSign, "GGG", "g0g", " T ", 'G', "blockGlass", 'g', "dustGlowstone", '0', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0), 'T', MatterOverdrive.ITEMS.tritanium_plate);
        addShapedRecipe(new ItemStack(MatterOverdrive.BLOCKS.forceGlass, 4), " G ", "GTG", " G ", 'G', Blocks.GLASS, 'T', MatterOverdrive.ITEMS.tritanium_plate);
        addShapedRecipe(MatterOverdrive.BLOCKS.tritaniumCrate, "   ", "TCT", " T ", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'C', Blocks.CHEST);
        addShapelessRecipe(new ItemStack(MatterOverdrive.BLOCKS.tritaniumCrateYellow), new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()), MatterOverdrive.BLOCKS.tritaniumCrate);
        addShapedOreRecipe(MatterOverdrive.BLOCKS.inscriber, "IDI", "TPT", "RMR", 'M', MatterOverdrive.ITEMS.machine_casing, 'D', "gemDilithium", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'P', Blocks.PISTON, 'R', "dustRedstone", 'I', "ingotIron");
        addShapedOreRecipe(MatterOverdrive.BLOCKS.fusionReactorIO, "TGT", "C C", "TGT", 'G', "ingotGold", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0));
        addShapedOreRecipe(MatterOverdrive.BLOCKS.contractMarket, " T ", "GEG", " M ", 'T', "ingotTritanium", 'G', "ingotGold", 'E', "gemEmerald", 'M', MatterOverdrive.ITEMS.machine_casing);
        addShapedOreRecipe(MatterOverdrive.BLOCKS.spacetimeAccelerator, "THT", "DDD", "THT", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'H', MatterOverdrive.ITEMS.h_compensator, 'D', "gemDilithium");

        addShapedRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_tritanium_plate, 16), "SSS", "S#S", "SSS", '#', MatterOverdrive.ITEMS.tritanium_plate, 'S', Blocks.STONE);
        addShapedOreRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_beams, 6), "#T#", "#T#", "#T#", '#', MatterOverdrive.BLOCKS.decorative_tritanium_plate, 'T', "nuggetTritanium");
        addShapedRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_tritanium_plate_stripe, 8), "###", "###", "#Y#", '#', MatterOverdrive.BLOCKS.decorative_tritanium_plate, 'Y', new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()), 'B', new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()));
        addShapedRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_holo_matrix, 8), "###", "#I#", "###", '#', MatterOverdrive.BLOCKS.decorative_tritanium_plate, 'I', MatterOverdrive.ITEMS.isolinear_circuit);
        addShapedRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_carbon_fiber_plate, 8), "###", "#C#", "###", '#', MatterOverdrive.BLOCKS.decorative_tritanium_plate, 'C', Items.COAL);
        addShapedOreRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_vent_bright, 6), " # ", "T T", " # ", '#', MatterOverdrive.ITEMS.tritanium_plate, 'T', "ingotTritanium");
        addShapedRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_vent_dark, 8), "###", "#B#", "###", '#', MatterOverdrive.BLOCKS.decorative_vent_bright, 'B', Items.DYE);
        addShapedRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_clean, 8), " S ", "STS", " S ", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'S', Blocks.STONE);
        addShapedOreRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_floor_tiles, 32), "###", "#Q#", "###", '#', Blocks.CLAY, 'Q', "gemQuartz");
        addShapedOreRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_floor_tiles_green, 32), "#G#", "#Q#", "#G#", '#', Blocks.CLAY, 'Q', "gemQuartz", 'G', new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()));
        addShapedOreRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_floor_tile_white, 32), "#W#", "#Q#", "#W#", '#', Blocks.CLAY, 'Q', "gemQuartz", 'W', new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
        addShapedOreRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_separator, 8), "###", "#N#", "###", '#', MatterOverdrive.BLOCKS.decorative_tritanium_plate, 'N', "nuggetTritanium");
        addShapedRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_floor_noise, 32), "#G#", "#Q#", "#G#", '#', Blocks.CLAY, 'B', Items.BONE, 'G', Blocks.GRAVEL);
        addShapedRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_white_plate, 8), "#W#", "###", "#W#", '#', MatterOverdrive.BLOCKS.decorative_tritanium_plate, 'W', Blocks.WOOL);
        addShapedRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_coils, 12), "###", "#C#", "###", '#', MatterOverdrive.BLOCKS.decorative_tritanium_plate, 'C', MatterOverdrive.ITEMS.s_magnet);
        addShapedRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_stripes, 8), "#B#", "###", "#Y#", '#', MatterOverdrive.BLOCKS.decorative_tritanium_plate, 'B', new ItemStack(Items.DYE), 'Y', new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()));
        addShapedOreRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_tritanium_lamp, 2), "###", "#G#", "GGG", '#', MatterOverdrive.BLOCKS.decorative_tritanium_plate, 'G', "dustGlowstone");
        for (EnumDyeColor dyeColor : EnumDyeColor.values()) {
            addShapedRecipe(new ItemStack(MatterOverdrive.BLOCKS.decorative_tritanium_plate_colored, 8, dyeColor.getDyeDamage()), "###", "#D#", "###", '#', MatterOverdrive.BLOCKS.decorative_tritanium_plate, 'D', new ItemStack(Items.DYE, 1, dyeColor.getDyeDamage()));
        }

    }

    public static void registerItemRecipes(FMLInitializationEvent event) {
        addShapedOreRecipe(MatterOverdrive.ITEMS.battery, " R ", "TGT", "TDT", 'T', "ingotTritanium", 'D', "gemDilithium", 'R', "dustRedstone", 'G', "ingotGold");
        addShapedOreRecipe(MatterOverdrive.ITEMS.hc_battery, " P ", "DBD", " P ", 'B', MatterOverdrive.ITEMS.battery, 'D', "gemDilithium", 'P', MatterOverdrive.ITEMS.tritanium_plate);
        addShapedOreRecipe(MatterOverdrive.ITEMS.matter_scanner, "III", "GDG", "IRI", 'I', "ingotIron", 'D', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), 'R', "dustRedstone", 'G', "ingotGold");
        addShapedOreRecipe(MatterOverdrive.ITEMS.h_compensator, " M ", "CPC", "DED", 'D', "gemDilithium", 'M', MatterOverdrive.ITEMS.machine_casing, 'I', "ingotIron", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0), 'P', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), 'E', Items.ENDER_EYE);
        addShapedOreRecipe(MatterOverdrive.ITEMS.integration_matrix, " M ", "GPG", "DED", 'G', "blockGlass", 'M', MatterOverdrive.ITEMS.machine_casing, 'I', "ingotIron", 'P', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), 'E', "enderpearl", 'D', "gemDilithium");
        addShapedOreRecipe(MatterOverdrive.ITEMS.machine_casing, " T ", "I I", "GRG", 'G', "ingotGold", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'I', "ingotTritanium", 'R', "dustRedstone");
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.s_magnet, 4), "RRR", "TET", "RRR", 'E', "enderpearl", 'T', "ingotTritanium", 'R', "dustRedstone");
        addShapedOreRecipe(MatterOverdrive.ITEMS.me_conversion_matrix, "EIE", "CDC", "EIE", 'E', "enderpearl", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), 'I', "ingotIron", 'D', "gemDilithium");
        addShapedOreRecipe(MatterOverdrive.ITEMS.tritanium_plate, "TT", 'T', "ingotTritanium");
        addShapedOreRecipe(MatterOverdrive.ITEMS.phaser, "IGI", "IPH", "WCW", 'I', "ingotIron", 'G', "blockGlass", 'P', MatterOverdrive.ITEMS.plasmaCore, 'W', Blocks.WOOL, 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), 'H', MatterOverdrive.ITEMS.weaponHandle);
        addShapedOreRecipe(MatterOverdrive.ITEMS.pattern_drive, " E ", "RMR", " C ", 'M', MatterOverdrive.ITEMS.machine_casing, 'E', "enderpearl", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), 'R', "dustRedstone");
        addShapedOreRecipe(MatterOverdrive.ITEMS.security_protocol, "PP", "CP", 'P', "paper", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0));
        addShapedOreRecipe(MatterOverdrive.ITEMS.wrench, "T T", " Y ", " T ", 'T', "ingotTritanium", 'Y', new ItemStack(Blocks.WOOL, 1, 4));
        addShapedRecipe(MatterOverdrive.ITEMS.spacetime_equalizer, " M ", "EHE", " M ", 'M', MatterOverdrive.ITEMS.s_magnet, 'E', Items.ENDER_PEARL, 'H', MatterOverdrive.ITEMS.h_compensator);
        addShapedOreRecipe(MatterOverdrive.ITEMS.forceFieldEmitter, "CGC", "CDC", "P1P", 'P', MatterOverdrive.ITEMS.tritanium_plate, 'E', "enderpearl", 'D', "gemDilithium", '1', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1), 'C', MatterOverdrive.ITEMS.s_magnet, 'G', "blockGlass");
        addShapedOreRecipe(MatterOverdrive.ITEMS.networkFlashDrive, "RCR", 'R', "dustRedstone", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0));
        addShapedOreRecipe(MatterOverdrive.ITEMS.transportFlashDrive, " I ", "ECR", " I ", 'I', "ingotIron", 'R', "dustRedstone", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0));
        //addRecipe(new EnergyPackRecipe(new ItemStack(MatterOverdrive.items.tritanium_plate), new ItemStack(MatterOverdrive.items.battery), new ItemStack(Items.GUNPOWDER)));
        //addRecipe(new EnergyPackRecipe(new ItemStack(MatterOverdrive.items.tritanium_plate), new ItemStack(MatterOverdrive.items.hc_battery), new ItemStack(Items.GUNPOWDER)));
        addShapedOreRecipe(MatterOverdrive.ITEMS.phaserRifle, "III", "SPC", "WHB", 'I', Items.IRON_INGOT, 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), 'W', Blocks.WOOL, 'S', MatterOverdrive.ITEMS.weaponReceiver, 'D', "gemDilithium", 'P', MatterOverdrive.ITEMS.plasmaCore, 'B', MatterOverdrive.ITEMS.battery, 'H', MatterOverdrive.ITEMS.weaponHandle);
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.matterContainer, 4), "TMT", " T ", 'T', "ingotTritanium", 'M', MatterOverdrive.ITEMS.s_magnet);
        addShapedOreRecipe(MatterOverdrive.ITEMS.tritanium_ingot, "###", "###", "###", '#', "nuggetTritanium");
        addShapelessOreRecipe(new ItemStack(MatterOverdrive.ITEMS.tritanium_nugget, 9), "ingotTritanium");
        addShapelessRecipe(MatterOverdrive.ITEMS.dataPad, Items.BOOK, new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 0));
        addShapedOreRecipe(MatterOverdrive.ITEMS.omniTool, "IFC", "SPI", " BH", 'I', "ingotIron", 'S', MatterOverdrive.ITEMS.weaponReceiver, 'P', MatterOverdrive.ITEMS.plasmaCore, 'B', MatterOverdrive.ITEMS.battery, 'F', MatterOverdrive.ITEMS.forceFieldEmitter, 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), 'H', MatterOverdrive.ITEMS.weaponHandle);
        addShapedOreRecipe(MatterOverdrive.ITEMS.tritaniumAxe, "XX ", "X# ", " # ", 'X', "ingotTritanium", '#', "stickWood");
        addShapedOreRecipe(MatterOverdrive.ITEMS.tritaniumPickaxe, "XXX", " # ", " # ", 'X', "ingotTritanium", '#', "stickWood");
        addShapedOreRecipe(MatterOverdrive.ITEMS.tritaniumSword, " X ", " X ", " # ", 'X', "ingotTritanium", '#', "stickWood");
        addShapedOreRecipe(MatterOverdrive.ITEMS.tritaniumHoe, "XX ", " # ", " # ", 'X', "ingotTritanium", '#', "stickWood");
        addShapedOreRecipe(MatterOverdrive.ITEMS.tritaniumShovel, " X ", " # ", " # ", 'X', "ingotTritanium", '#', "stickWood");
        addShapedOreRecipe(MatterOverdrive.ITEMS.tritaniumHelmet, "XCX", "X X", "   ", 'X', "ingotTritanium", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1));
        addShapedOreRecipe(MatterOverdrive.ITEMS.tritaniumChestplate, "X X", "XCX", "XXX", 'X', "ingotTritanium", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1));
        addShapedOreRecipe(MatterOverdrive.ITEMS.tritaniumLeggings, "XCX", "X X", "X X", 'X', "ingotTritanium", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1));
        addShapedOreRecipe(MatterOverdrive.ITEMS.tritaniumBoots, "   ", "X X", "X X", 'X', "ingotTritanium");
        addShapedOreRecipe(MatterOverdrive.ITEMS.isolinear_circuit, "I", "R", "G", 'G', "blockGlass", 'R', "dustRedstone", 'I', "ingotIron");
        addShapedOreRecipe(MatterOverdrive.ITEMS.sniperScope, "IIC", "GFG", "III", 'I', "ingotIron", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 1), 'G', new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 5), 'F', MatterOverdrive.ITEMS.forceFieldEmitter);
        addShapedOreRecipe(MatterOverdrive.ITEMS.weaponHandle, "TWT", "I I", "I I", 'I', "ingotIron", 'W', new ItemStack(Blocks.WOOL, 1, 15), 'T', "ingotTritanium");
        addShapedOreRecipe(MatterOverdrive.ITEMS.weaponReceiver, "IRT", "   ", "IIT", 'I', "ingotIron", 'R', "dustRedstone", 'T', "ingotTritanium");
        addShapedOreRecipe(MatterOverdrive.ITEMS.plasmaCore, "GI ", "MCM", " IG", 'G', "blockGlass", 'I', "ingotIron", 'M', MatterOverdrive.ITEMS.s_magnet, 'C', new ItemStack(MatterOverdrive.ITEMS.matterContainer));
        addShapedOreRecipe(MatterOverdrive.ITEMS.plasmaShotgun, "SP ", "ICH", "SPB", 'S', MatterOverdrive.ITEMS.weaponReceiver, 'P', MatterOverdrive.ITEMS.plasmaCore, 'I', "ingotIron", 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2), 'B', MatterOverdrive.ITEMS.battery, 'H', MatterOverdrive.ITEMS.weaponHandle);
        addShapedOreRecipe(MatterOverdrive.ITEMS.ionSniper, "ICI", "SPP", " HB", 'I', "ingotIron", 'S', MatterOverdrive.ITEMS.weaponReceiver, 'P', MatterOverdrive.ITEMS.plasmaCore, 'H', MatterOverdrive.ITEMS.weaponHandle, 'B', MatterOverdrive.ITEMS.battery, 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 3));
        addShapedRecipe(MatterOverdrive.ITEMS.portableDecomposer, " T ", "IPM", " T ", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'I', MatterOverdrive.ITEMS.integration_matrix, 'M', MatterOverdrive.ITEMS.me_conversion_matrix, 'P', Blocks.STICKY_PISTON);
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.item_upgrade, 1, 0), " R ", " C ", " T ", 'G', "blockGlass", 'R', "dustRedstone", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'C', new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 0));
        //speed
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.item_upgrade, 1, 1), " R ", "GUG", " E ", 'U', MatterOverdrive.ITEMS.item_upgrade, 'G', "dustGlowstone", 'R', "dustRedstone", 'E', "gemEmerald");
        //power
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.item_upgrade, 1, 2), " B ", "RUR", " C ", 'U', MatterOverdrive.ITEMS.item_upgrade, 'B', MatterOverdrive.ITEMS.battery, 'R', "dustRedstone", 'C', "gemQuartz");
        //failsafe
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.item_upgrade, 1, 3), " D ", "RUR", " G ", 'U', MatterOverdrive.ITEMS.item_upgrade, 'D', "gemDiamond", 'R', "dustRedstone", 'G', "ingotGold");
        //range
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.item_upgrade, 1, 4), " E ", "RUR", " G ", 'U', MatterOverdrive.ITEMS.item_upgrade, 'E', "enderpearl", 'R', "dustRedstone", 'G', "ingotGold");
        //power storage
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.item_upgrade, 1, 5), "   ", "RUR", " B ", 'U', MatterOverdrive.ITEMS.item_upgrade, 'B', MatterOverdrive.ITEMS.hc_battery, 'R', "dustRedstone", 'G', "ingotGold");
        //hyper speed

        //TODO: move to events/json
        //addRecipe(new ShapelessOreRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 6), "gemDilithium", Items.NETHER_STAR, new ItemStack(MatterOverdrive.items.item_upgrade, 1, 1)));
        //matter storage
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.item_upgrade, 1, 7), " R ", "MUM", " R ", 'U', MatterOverdrive.ITEMS.item_upgrade, 'M', MatterOverdrive.ITEMS.s_magnet, 'R', "dustRedstone");
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.weapon_module_barrel, 1, 0), " G ", "RDR", " T ", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'D', "gemDilithium", 'R', "dustRedstone", 'G', "blockGlass");
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.weapon_module_barrel, 1, 1), " G ", "BFB", " T ", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'F', Items.FIRE_CHARGE, 'B', Items.BLAZE_ROD, 'G', "blockGlass");
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.weapon_module_barrel, 1, 2), " B ", "BRB", "DTD", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'R', Items.BLAZE_ROD, 'B', Blocks.TNT, 'G', "blockGlass", 'D', "gemDiamond");
        addShapedOreRecipe(new ItemStack(MatterOverdrive.ITEMS.weapon_module_barrel, 1, 3), " S ", "SAS", "ETE", 'T', MatterOverdrive.ITEMS.tritanium_plate, 'A', Items.GOLDEN_APPLE, 'S', Items.SUGAR, 'G', "blockGlass", 'E', "gemEmerald");

        RecipeSorter.register("matteroverdrive:energyPack", EnergyPackRecipe.class, RecipeSorter.Category.SHAPELESS, "");
    }

    public static void registerInscriberRecipes(FMLInitializationEvent event) {
        File file = new File(MatterOverdrive.configHandler.configDir, "MatterOverdrive/recipes/inscriber.xml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                IOUtils.copy(MatterOverdriveRecipes.class.getResourceAsStream("/assets/matteroverdrive/recipes/inscriber.xml"), new FileOutputStream(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //TO: Recipes
        INSCRIBER.load(file);
    }

    private static void addShapedOreRecipe(ItemStack output, Object... params) {
        //TODO: move to events/json
        //addRecipe(new ShapedOreRecipe(output, params));
    }

    private static void addShapedOreRecipe(Item output, Object... params) {
        addShapedOreRecipe(new ItemStack(output), params);
    }

    private static void addShapedOreRecipe(Block output, Object... params) {
        addShapedOreRecipe(new ItemStack(output), params);
    }

    private static void addShapelessOreRecipe(ItemStack output, Object... params) {
        //TODO: move to events/json
        //addRecipe(new ShapelessOreRecipe(output, params));
    }

    private static void addShapelessOreRecipe(Item output, Object... params) {
        addShapelessOreRecipe(new ItemStack(output), params);
    }

    private static void addShapelessOreRecipe(Block output, Object... params) {
        addShapedOreRecipe(new ItemStack(output), params);
    }

    private static void addShapedRecipe(ItemStack output, Object... params) {
        //TODO: move to events/json
        //recipes.add(GameRegistry.addShapedRecipe(output, params););
    }

    private static void addShapedRecipe(Item output, Object... params) {
        addShapedRecipe(new ItemStack(output), params);
    }

    private static void addShapedRecipe(Block output, Object... params) {
        addShapedRecipe(new ItemStack(output), params);
    }

    private static void addShapelessRecipe(ItemStack output, Object... items) {
        //TODO: move to events/json
        //GameRegistry.addShapelessRecipe(output, items);
    }

    private static void addShapelessRecipe(Item output, Object... params) {
        addShapelessRecipe(new ItemStack(output), params);
    }

    private static void addShapelessRecipe(Block output, Object... params) {
        addShapelessRecipe(new ItemStack(output), params);
    }

    private static void addRecipe(IRecipe recipe) {
        recipes.add(recipe);
        GameData.register_impl(recipe);
    }
}
