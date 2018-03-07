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

package matteroverdrive.proxy;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.compat.MatterOverdriveCompat;
import matteroverdrive.handler.GoogleAnalyticsCommon;
import matteroverdrive.handler.weapon.CommonWeaponHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {
    private final CommonWeaponHandler commonWeaponHandler;
    protected GoogleAnalyticsCommon googleAnalytics;

    public CommonProxy() {
        commonWeaponHandler = new CommonWeaponHandler();
        googleAnalytics = new GoogleAnalyticsCommon();
    }

    public void registerCompatModules() {
        MatterOverdriveCompat.registerModules();
    }

    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().player;
    }

    public void preInit(FMLPreInitializationEvent event) {
        registerCompatModules();
    }

    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(getWeaponHandler());
        MatterOverdrive.CONFIG_HANDLER.subscribe(googleAnalytics);
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public CommonWeaponHandler getWeaponHandler() {
        return commonWeaponHandler;
    }

    public GoogleAnalyticsCommon getGoogleAnalytics() {
        return googleAnalytics;
    }

    public boolean hasTranslation(String key) {
        return I18n.canTranslate(key);
    }

    public String translateToLocal(String key, Object... params) {
        return I18n.translateToLocalFormatted(key, params);
    }

}
