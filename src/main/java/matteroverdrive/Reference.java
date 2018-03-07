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

package matteroverdrive;

import matteroverdrive.client.data.Color;

public final class Reference {
    private Reference() {
    }

    public static final String MOD_ID = "matteroverdrive";
    public static final String MOD_NAME = "MatterOverdrive";
    public static final String VERSION = "@VERSION@";
    public static final String VERSION_DATE = "@DATE@";
    public static final String GUI_FACTORY_CLASS = "matteroverdrive.gui.GuiConfigFactory";
    public static final String DEPENDENCIES = "required-after:clib@[1.0.2.31,)";
    public static final String CHANNEL_NAME = "mo_channel";
    public static final String CHANNEL_WEAPONS_NAME = CHANNEL_NAME + ":weapons";
    public static final String CHANNEL_GUI_NAME = CHANNEL_NAME + ":gui";
    public static final String VERSIONS_CHECK_URL = "https://api.cfwidget.com/mc-mods/minecraft/229694-matter-overdrive";
    public static final String BETA_CHECK_URL = "http://maven.k-4u.nl/matteroverdrive/MatterOverdrive/maven-metadata.xml";
    public static final String DOWNLOAD_URL = "https://minecraft.curseforge.com/projects/matter-overdrive";

    //region GUI
    public static final String PATH_GFX = MOD_ID+":textures/";
    public static final String PATH_WORLD_TEXTURES = PATH_GFX + "world/";
    public static final String PATH_ARMOR = PATH_GFX + "armor/";
    public static final String PATH_PARTICLE = PATH_GFX + "particle/";
    public static final String PATH_GUI = PATH_GFX + "gui/";
    public static final String PATH_BLOCKS = PATH_GFX + "blocks/";
    public static final String PATH_FX = PATH_GFX + "fx/";
    public static final String PATH_SHADERS = MOD_ID+":shaders/";
    public static final String PATH_ELEMENTS = PATH_GUI + "elements/";
    public static final String PATH_ICON = PATH_GUI + "icons/";
    public static final String PATH_ENTITIES = PATH_GFX + "entities/";
    public static final String PATH_ITEM = PATH_GFX + "items/";
    public static final String PATH_GUI_ITEM = PATH_GUI + "items/";
    public static final String PATH_MODEL = MOD_ID+":models/";
    public static final String PATH_MODEL_BLOCKS = PATH_MODEL + "block/";
    public static final String PATH_MODEL_ITEMS = PATH_MODEL + "item/";
    public static final String PATH_SOUNDS = MOD_ID+":sounds/";
    public static final String PATH_SOUNDS_BLOCKS = PATH_SOUNDS + "blocks/";
    public static final String PATH_INFO = MOD_ID+":info/";
    //endregion

    //region GUI Textures
    public static final String TEXTURE_ARROW_PROGRESS = PATH_ELEMENTS + "progress_arrow_right.png";
    public static final String TEXTURE_FE_METER = PATH_ELEMENTS + "fe.png";

    //region Colors
    public static final Color COLOR_WHITE = new Color(255, 255, 255);
    public static final Color COLOR_MATTER = new Color(191, 228, 230);
    public static final Color COLOR_HOLO = new Color(169, 226, 251);
    public static final Color COLOR_YELLOW_STRIPES = new Color(254, 203, 4);
    public static final Color COLOR_HOLO_RED = new Color(230, 80, 20);
    public static final Color COLOR_HOLO_GREEN = new Color(24, 207, 0);
    public static final Color COLOR_HOLO_YELLOW = new Color(252, 223, 116);
    public static final Color COLOR_HOLO_PURPLE = new Color(116, 23, 230);
    public static final Color COLOR_GUI_NORMAL = new Color(62, 81, 84);
    public static final Color COLOR_GUI_LIGHT = new Color(100, 113, 136);
    public static final Color COLOR_GUI_LIGHTER = new Color(139, 126, 168);
    public static final Color COLOR_GUI_DARK = new Color(44, 54, 52);
    public static final Color COLOR_GUI_DARKER = new Color(34, 40, 37);
    public static final Color COLOR_GUI_ENERGY = new Color(224, 0, 0);
    //endregion

    //region Modules
    public static final int MODULE_BATTERY = 0;
    public static final int MODULE_COLOR = 1;
    public static final int MODULE_BARREL = 2;
    public static final int MODULE_SIGHTS = 3;
    public static final int MODULE_OTHER = 4;
    //end region

    //region Bionic Types
    public static final int BIONIC_HEAD = 0;
    public static final int BIONIC_ARMS = 1;
    public static final int BIONIC_LEGS = 2;
    public static final int BIONIC_CHEST = 3;
    public static final int BIONIC_OTHER = 4;
    public static final int BIONIC_BATTERY = 5;
    //endregion

    //region Weapon Stat
    public static final int WS_DAMAGE = 0;
    public static final int WS_AMMO = 1;
    public static final int WS_EFFECT = 2;
    public static final int WS_RANGE = 3;
    public static final int WS_FIRE_DAMAGE = 4;
    public static final int WS_BLOCK_DAMAGE = 5;
    public static final int WS_EXPLOSION_DAMAGE = 6;
    public static final int WS_FIRE_RATE = 7;
    public static final int WS_HEAL = 8;
    public static final int WS_MAX_HEAT = 9;
    public static final int WS_ACCURACY = 10;
    public static final int WS_SHOOT_COOLDOWN = 11;
    public static final int WS_RICOCHET = 12;
    //endregion

    //region Request Packet Type
    public static final int PACKET_REQUEST_CONNECTION = 0;
    public static final int PACKET_REQUEST_PATTERN_SEARCH = 1;
    public static final int PACKET_REQUEST_NEIGHBOR_CONNECTION = 2;
    public static final int PACKET_REQUEST_VALID_PATTERN_DESTINATION = 3;
    //endregion

    //region Broadcast Packet Type
    public static final int PACKET_BROADCAST_CONNECTION = 0;
    //endregion

    //region Packet Responce Type
    public static final int PACKET_RESPONCE_ERROR = -1;
    public static final int PACKET_RESPONCE_INVALID = 0;
    public static final int PACKET_RESPONCE_VALID = 1;
    //endregion

    //region machine mods
    public static final byte MODE_REDSTONE_NONE = 2;
    public static final byte MODE_REDSTONE_HIGH = 1;
    public static final byte MODE_REDSTONE_LOW = 0;
    //endregion

    //region models
    public static final String MODEL_SPHERE = PATH_MODEL_BLOCKS + "sphere.obj";
    public static final String MODEL_CHARGING_STATION = PATH_MODEL_BLOCKS + "charging_station.obj";
    public static final String MODEL_PATTERN_STORAGE = PATH_MODEL_BLOCKS + "pattern_storage.obj";
    public static final String MODEL_REPLICATOR = PATH_MODEL_BLOCKS + "replicator.obj";
    public static final String MODEL_TRITANIUM_CRATE = PATH_MODEL_BLOCKS + "tritanium_crate.obj";
    public static final String MODEL_INSCRIBER = PATH_MODEL_BLOCKS + "inscriber.obj";
    //endregion

    //region config keys
    public static final String CONFIG_KEY_REDSTONE_MODE = "redstoneMode";
    //endregion

    //region World Geb
    public static final String CHEST_GEN_ANDROID_HOUSE = "android_house";
    public static final String WORLD_DATA_MO_GEN_POSITIONS = "MatterOverdriveWorldGenPositions";
    //endregion

    //region Unicodes
    public static final String UNICODE_LEGENDARY = "\u272a";
    public static final String UNICODE_COMPLETED_OBJECTIVE = "\u25a0";
    public static final String UNICODE_UNCOMPLETED_OBJECTIVE = "\u25a1";
    //endregion
}
