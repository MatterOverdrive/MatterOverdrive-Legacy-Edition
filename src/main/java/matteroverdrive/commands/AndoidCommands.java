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

package matteroverdrive.commands;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Simeon on 5/26/2015.
 */
public class AndoidCommands extends CommandBase {
    @Override
    public String getName() {
        return "android";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "android <command> <value> <player>";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandException {
        if (parameters.length == 0) {
            sender.sendMessage(new TextComponentString("Invalid Parameters"));
            return;
        }

        if (parameters.length >= 2) {
            EntityPlayer player;
            if (parameters.length >= 3) {
                player = getPlayer(server, sender, parameters[2]);
            } else {
                player = getCommandSenderAsPlayer(sender);
            }

            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
            if (androidPlayer != null) {

                boolean validCommand = false;
                String commandInfo = "";

                if (parameters[0].equalsIgnoreCase("set")) {
                    boolean android = parseBoolean(parameters[1]);
                    androidPlayer.setAndroid(android);
                    validCommand = true;
                    if (android) {
                        commandInfo = sender.getName() + " is now an Android";
                    } else {
                        commandInfo = sender.getName() + " is no longer an Android";
                    }
                } else if (parameters[0].equalsIgnoreCase("stats")) {
                    if (parameters[1].equalsIgnoreCase("reset")) {
                        androidPlayer.resetUnlocked();
                        validCommand = true;
                        commandInfo = sender.getName() + " stats are now Reset";
                    }
                } else if (parameters[0].equalsIgnoreCase("unlock")) {
                    if (MatterOverdrive.STAT_REGISTRY.hasStat(parameters[1])) {
                        IBioticStat stat = MatterOverdrive.STAT_REGISTRY.getStat(parameters[1]);
                        androidPlayer.unlock(stat, stat.maxLevel());
                        validCommand = true;
                        commandInfo = sender.getName() + " now has the ability " + TextFormatting.GREEN + "[" + stat.getDisplayName(androidPlayer, stat.maxLevel()) + "]";
                    }
                } else if (parameters[0].equalsIgnoreCase("forget")) {
                    if (MatterOverdrive.STAT_REGISTRY.hasStat(parameters[1])) {
                        IBioticStat stat = MatterOverdrive.STAT_REGISTRY.getStat(parameters[1]);
                        androidPlayer.reset(stat);
                        validCommand = true;
                        commandInfo = TextFormatting.GREEN + "[" + stat.getDisplayName(androidPlayer, stat.maxLevel()) + "]" + TextFormatting.RESET + " removed from " + sender.getName();
                    }
                }

                if (validCommand) {
                    androidPlayer.sync(EnumSet.allOf(AndroidPlayer.DataType.class), false);
                    sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "[" + Reference.MOD_NAME + "] " + TextFormatting.RESET + commandInfo));
                    return;
                }
            }
        }

        sender.sendMessage(new TextComponentString("Invalid Android Command. Use /help to learn more."));
    }


    public static String[] subCommands = new String[]{
            "set", "stats", "unlock", "forget"
    };

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] parameters, @Nullable BlockPos targetPos) {
        List<String> commands = new ArrayList<>();

        if (parameters.length == 2) {
            if (parameters[0].equalsIgnoreCase("set")) {
                if ("true".startsWith(parameters[1]))
                    commands.add("true");
                if ("false".startsWith(parameters[1]))
                    commands.add("false");
            } else if (parameters[0].equalsIgnoreCase("stats")) {
                if ("reset".startsWith(parameters[1]))
                    commands.add("reset");
            } else if (parameters[0].equalsIgnoreCase("unlock")||parameters[0].equalsIgnoreCase("forget")) {
                commands.addAll(MatterOverdrive.STAT_REGISTRY.getStats().stream().map(IBioticStat::getUnlocalizedName).filter(s -> s.startsWith(parameters[1])).collect(Collectors.toList()));
            }
        } else {
            for (String s : subCommands)
                if (s.startsWith(parameters[0]))
                    commands.add(s);
        }
        return commands;
    }

    @Override
    public boolean isUsernameIndex(String[] params, int index) {
        return index == 2;
    }
}
