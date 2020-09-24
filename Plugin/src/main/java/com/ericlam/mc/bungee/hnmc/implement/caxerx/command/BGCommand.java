package com.ericlam.mc.bungee.hnmc.implement.caxerx.command;

import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandNode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class BGCommand extends Command implements TabExecutor {

    private CommandExecutor executor;

    BGCommand(CommandNode node, CommandExecutor executor) {
        super(node.getCommand(), node.getPermission(), node.getAlias().toArray(String[]::new));
        this.executor = executor;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        executor.handle(commandSender, this, strings);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return executor.onTabComplete(commandSender, this, strings);
    }
}
