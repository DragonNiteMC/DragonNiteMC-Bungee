package com.ericlam.mc.bungee.hnmc.implement.caxerx.command;

import com.ericlam.mc.bungee.hnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandRegister;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.exception.CommandArgumentException;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.exception.CommandPermissionException;
import com.ericlam.mc.bungee.hnmc.config.MainConfig;
import com.ericlam.mc.bungee.hnmc.permission.Perm;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandExecutor implements CommandRegister {
    private List<CommandNode> registeredCommand = new ArrayList<>();

    @Inject
    private MainConfig mainConfig;

    @Override
    public void registerCommand(Plugin plugin, CommandNode node) {
        if (node.getParent() != null) {
            ProxyServer.getInstance().getLogger().warning("Command [/"+node.getCommand()+"] cannot register to MainCommand because it has it's own parent.");
            return;
        }
        registeredCommand.add(node);
        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new BGCommand(node, this));
    }

    void handle(CommandSender sender, Command command, String[] args) {
        try {
            for (CommandNode cmd : registeredCommand) {
                if (cmd.match(command.getName())) {
                    cmd.invokeCommand(sender, new LinkedList<>(Arrays.asList(args)));
                    return;
                }
            }
        } catch (CommandPermissionException e) {
            MessageBuilder.sendMessage(sender,mainConfig.getPrefix()+mainConfig.getNoPermission());
            if (!Perm.hasPermission(sender,Perm.HELPER)) return;
            MessageBuilder.sendMessage(sender, mainConfig.getPrefix()+ChatColor.RED+"缺少權限: " + e.getMessage());
        } catch (CommandArgumentException e) {
            MessageBuilder.sendMessage(sender, mainConfig.getPrefix()+ChatColor.RED+"缺少參數: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            MessageBuilder.sendMessage(sender, ChatColor.RED + "發生錯誤, 請將此告知于插件師。");
            MessageBuilder.sendMessage(sender, ChatColor.RED + e.toString());
        }
    }

    List<String> onTabComplete(CommandSender sender, Command command, String[] args) {
        try {
            for (CommandNode cmd : registeredCommand) {
                if (cmd.match(command.getName())) {
                    List<String> result = cmd.invokeTabCompletion(sender, Lists.newArrayList(args));
                    String lastAug = args[args.length - 1];
                    if (result != null && !lastAug.equals("")) {
                        result.removeIf(tabItem -> !tabItem.startsWith(lastAug));
                    }
                    return result == null ? new ArrayList<>() : result;
                }
            }
        } catch (CommandPermissionException | CommandArgumentException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            MessageBuilder.sendMessage(sender, ChatColor.RED + "發生錯誤, 請將此告知于插件師。");
            MessageBuilder.sendMessage(sender, ChatColor.RED + e.toString());
        }
        return new ArrayList<>();
    }
}
