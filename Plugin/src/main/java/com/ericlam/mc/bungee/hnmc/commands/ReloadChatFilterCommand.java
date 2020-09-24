package com.ericlam.mc.bungee.hnmc.commands;

import com.ericlam.mc.bungee.hnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.hnmc.main.HyperNiteMC;
import com.ericlam.mc.bungee.hnmc.permission.Perm;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;

public class ReloadChatFilterCommand extends CommandNode {


    public ReloadChatFilterCommand() {
        super(null, "reload-chat-filter", Perm.OWNER, "重載 chat-filter.yml", null, "reload-cf");
    }

    @Override
    public void executeCommand(CommandSender sender, List<String> args) {
        HyperNiteMC.getHnBungeeConfig().reloadChatFilter();
        MessageBuilder.sendMessage(sender, HyperNiteMC.getAPI().getMainConfig().getPrefix()+"§e重載成功");
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
