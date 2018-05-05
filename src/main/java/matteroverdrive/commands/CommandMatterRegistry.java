package matteroverdrive.commands;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.data.matter.DamageAwareStackHandler;
import matteroverdrive.data.matter.ItemHandler;
import matteroverdrive.data.matter.OreHandler;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveMatter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;

public class CommandMatterRegistry extends MOCommand {

    public CommandMatterRegistry() {
        super("matterregistry");
        addCommand(new SubCommand("recalculate") {
            @Override
            public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
                MatterOverdrive.MATTER_REGISTRY.getItemEntires().clear();
                MatterOverdriveMatter.registerBasic(MatterOverdrive.CONFIG_HANDLER);
                MatterOverdrive.MATTER_REGISTRATION_HANDLER.runCalculationThread(sender.getEntityWorld());
            }
        });
        addCommand(new SubCommand("blacklist") {
            @Override
            public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) throws CommandException {
                ItemStack stack;
                if (args.length >= 3) {
                    stack = getPlayer(server, commandSender, args[2]).getHeldItem(EnumHand.MAIN_HAND);
                } else {
                    stack = getPlayer(server, commandSender, commandSender.getName()).getHeldItem(EnumHand.MAIN_HAND);
                }

                String key;
                if (!stack.isEmpty()) {
                    if (args[1].equalsIgnoreCase("itemstack")) {
                        key = stack.getItem().getRegistryName() + "/" + stack.getItemDamage();
                        MatterOverdrive.MATTER_REGISTRY.register(stack.getItem(), new DamageAwareStackHandler(stack.getItemDamage(), 0, true));
                    } else if (args[1].equalsIgnoreCase("item")) {
                        key = stack.getItem().getRegistryName().toString();
                        MatterOverdrive.MATTER_REGISTRY.register(stack.getItem(), new ItemHandler(0, true));
                    } else if (args[1].equalsIgnoreCase("ore")) {
                        int[] orenames = OreDictionary.getOreIDs(stack);
                        if (orenames != null && orenames.length > 0) {
                            key = OreDictionary.getOreName(orenames[0]);
                            MatterOverdrive.MATTER_REGISTRY.registerOre(key, new OreHandler(0, true));
                        } else {
                            throw new CommandException("Could not find an ore dictionary entry", args[1]);
                        }
                    } else {
                        throw new CommandException("Invalid type of item. Use either item, itemstack or ore.");
                    }

                    String[] oldBlacklist = MatterOverdrive.CONFIG_HANDLER.getStringList(ConfigurationHandler.CATEGORY_MATTER, ConfigurationHandler.KEY_MBLACKLIST);
                    String[] newBlacklist = new String[oldBlacklist != null ? oldBlacklist.length + 1 : 1];
                    newBlacklist[oldBlacklist.length] = key;
                    MatterOverdrive.CONFIG_HANDLER.config.get(ConfigurationHandler.CATEGORY_MATTER, ConfigurationHandler.KEY_MBLACKLIST, new String[]{}, "").set(newBlacklist);
                    MatterOverdrive.CONFIG_HANDLER.save();
                    commandSender.sendMessage(new TextComponentString(TextFormatting.GOLD + "[" + key + "]" + TextFormatting.RESET + " Added $s to matter blacklist and config.\nYou must recalculate the registry for changes to take effect.\nUse /matter_registry recalculate."));
                    return;
                } else {
                    throw new CommandException("Player not holding any item", args[1]);
                }
            }
        });
        addCommand(new SubCommand("register") {
            @Override
            public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) throws CommandException {
                int matter = parseInt(args[2]);
                ItemStack stack;
                if (args.length >= 4) {
                    stack = getPlayer(server, commandSender, args[3]).getHeldItem(EnumHand.MAIN_HAND);
                } else {
                    stack = getPlayer(server, commandSender, commandSender.getName()).getHeldItem(EnumHand.MAIN_HAND);
                }

                if (!stack.isEmpty()) {
                    String key;
                    if (args[1].equalsIgnoreCase("itemstack")) {
                        key = stack.getItem().getRegistryName() + "/" + stack.getItemDamage();
                        MatterOverdrive.MATTER_REGISTRY.register(stack.getItem(), new DamageAwareStackHandler(stack.getItemDamage(), matter));

                    } else if (args[1].equalsIgnoreCase("item")) {
                        key = stack.getItem().getRegistryName().toString();
                        MatterOverdrive.MATTER_REGISTRY.register(stack.getItem(), new ItemHandler(matter));
                    } else if (args[1].equalsIgnoreCase("ore")) {
                        int[] oreNames = OreDictionary.getOreIDs(stack);
                        if (oreNames != null && oreNames.length > 0) {
                            key = OreDictionary.getOreName(oreNames[0]);
                        } else {
                            throw new CommandException("Could not find an ore dictionary entry!");
                        }
                    } else {
                        throw new CommandException("Invalid type of item. Use either item,itemstack or ore.");
                    }

                    MatterOverdrive.CONFIG_HANDLER.setInt(key, ConfigurationHandler.CATEGORY_MATTER_ITEMS, matter);
                    MatterOverdrive.CONFIG_HANDLER.save();
                    commandSender.sendMessage(new TextComponentString(TextFormatting.GOLD + "[" + key + "]" + TextFormatting.RESET + " Added $s to matter registry and config.\nYou can now recalculated the registry.\nUse /matter_registry recalculate."));
                } else {
                    throw new CommandException("Player iz not holding any item", args[1]);
                }
            }
        });
    }
}