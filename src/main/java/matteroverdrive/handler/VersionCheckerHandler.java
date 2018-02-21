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

package matteroverdrive.handler;

import com.astro.clib.util.Platform;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.handler.thread.VersionCheckThread;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;

import java.sql.Ref;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Simeon on 5/7/2015.
 */
public class VersionCheckerHandler implements IConfigSubscriber {
    public static final String[] mirrors = new String[]{Reference.VERSIONS_CHECK_URL};
    public Future<String> download;
    int lastPoll = 400;
    private boolean updateInfoDisplayed = false;
    private int currentMirror = 0;
    private boolean checkForUpdates;

    //Called when a player ticks.
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.phase != TickEvent.Phase.START || !checkForUpdates) {
            return;
        }

        if (FMLCommonHandler.instance().getSide() == Side.SERVER && FMLCommonHandler.instance().getMinecraftServerInstance().isServerRunning()) {
            if(!event.player.canUseCommand(2,""))
                return;
        }

        if (lastPoll > 0) {
            --lastPoll;
            return;
        }
        lastPoll = 400;

        if (updateInfoDisplayed) {
            return;
        }

        if (currentMirror < mirrors.length) {
            if (download == null) {
                download = MatterOverdrive.THREAD_POOL.submit(new VersionCheckThread(mirrors[currentMirror]));
                currentMirror++;
            }
        }

        if (download != null && download.isDone()) {
            String result = null;

            try {
                result = download.get();
            } catch (InterruptedException e) {
                MOLog.log(Level.ERROR, e, "Version checking from '%1$s' was interrupted", mirrors[currentMirror - 1]);
                MatterOverdrive.PROXY.getGoogleAnalytics().setExceptionHit("Version Checking interrupted");
            } catch (ExecutionException e) {
                MOLog.log(Level.ERROR, e, "Version checking from '%1$s' has failed", mirrors[currentMirror - 1]);
                MatterOverdrive.PROXY.getGoogleAnalytics().setExceptionHit("Version Checking failed");
            } finally {
                if (result != null) {
                    try {
                        updateInfoDisplayed = constructVersionAndCheck(result, event.player);
                    } catch (Exception e) {
                        MOLog.log(Level.ERROR, e, "There was a problem while decoding the update info from website.");
                    }
                }

                download.cancel(false);
                download = null;
            }
        }
    }
    final String regex = "([0-9])\\.([0-9])\\.([0-9])\\.*([0-9])*";
    final Pattern pattern = Pattern.compile(regex);

    private boolean constructVersionAndCheck(String jsonText, EntityPlayer player) {
        JsonParser parser = new JsonParser();

        JsonObject root = parser.parse(jsonText).getAsJsonObject();

        JsonObject versions = root.get("versions").getAsJsonObject();

        JsonArray versionData = versions.get(Loader.MC_VERSION).getAsJsonArray();

        JsonObject latest = versionData.get(0).getAsJsonObject();

        String type = latest.get("type").getAsString();
        String fileName = latest.get("name").getAsString();

        Matcher matcher = pattern.matcher(fileName);
        if (!matcher.find())
            return false;

        String fullVersion = matcher.group(0);

        boolean hasNew = false;
        if (!Platform.isDevEnv() && !Reference.VERSION.equals(fullVersion)) {
            Matcher currentMatcher = pattern.matcher(Reference.VERSION);
            if (Integer.parseInt(currentMatcher.group(1)) >= Integer.parseInt(matcher.group(1))) {
                if (Integer.parseInt(currentMatcher.group(2)) >= Integer.parseInt(matcher.group(2))) {
                    if (Integer.parseInt(currentMatcher.group(3)) >= Integer.parseInt(matcher.group(3))) {
                        if (matcher.groupCount() == 5 && currentMatcher.groupCount() == 5) {
                            if (Integer.parseInt(currentMatcher.group(4)) >= Integer.parseInt(matcher.group(4))) {
                                hasNew = true;
                            }
                        } else {
                            hasNew = true;
                        }
                    }
                }
            }
        }

        if (hasNew) {
            TextComponentString chat = new TextComponentString(TextFormatting.GOLD + "[Matter Overdrive] ");
            Style style = new Style();
            chat.appendSibling(new TextComponentTranslation("alert.new_update")).setStyle(style.setColor(TextFormatting.WHITE));
            player.sendMessage(chat);

            chat = new TextComponentString("");
            ITextComponent versionName = new TextComponentString(root.get("title").getAsString() + " ").setStyle(new Style().setColor(TextFormatting.AQUA));
            chat.appendSibling(versionName);
            chat.appendText(TextFormatting.WHITE + "[");
            style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Reference.DOWNLOAD_URL));
            style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("info." + Reference.MOD_ID + ".updater.hover").setStyle(new Style().setColor(TextFormatting.YELLOW))));
            style.setColor(TextFormatting.GREEN);
            chat.appendSibling(new TextComponentTranslation("info." + Reference.MOD_ID + ".updater.download")).setStyle(style);
            chat.appendText(TextFormatting.WHITE + "]");
            player.sendMessage(chat);
            return true;
        } else {
            MOLog.info("Matter Overdrive Version %1$s is up to date. Currently '%2$s'", fullVersion, Reference.VERSION);
        }
        return false;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        String comment = "Should Matter Overdrive check for newer versions, every time the world starts";
        checkForUpdates = config.getBool(ConfigurationHandler.KEY_VERSION_CHECK, ConfigurationHandler.CATEGORY_CLIENT, true, comment);
        config.config.get(ConfigurationHandler.CATEGORY_CLIENT, ConfigurationHandler.KEY_VERSION_CHECK, true).setComment(comment);
    }
}
