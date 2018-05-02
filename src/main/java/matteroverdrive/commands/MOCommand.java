package matteroverdrive.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MOCommand extends CommandBase {
    private final String name;
    private final List<SubCommand> subCommands = new ArrayList<>();

    public MOCommand(String name) {
        this.name = name;
    }

    public void addCommand(SubCommand command) {
        subCommands.add(command);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/" + getName() + " <subcommand>";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] args) throws CommandException {
        if (args.length == 0) return;
        for (SubCommand command : subCommands) {
            if (command.getName().equalsIgnoreCase(args[0])) {
                command.execute(minecraftServer, iCommandSender, args);
                return;
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (SubCommand command : subCommands) {
                if (command.getName().startsWith(args[0].toLowerCase()))
                    completions.add(command.getName());
            }
        }
        return completions;
    }
}