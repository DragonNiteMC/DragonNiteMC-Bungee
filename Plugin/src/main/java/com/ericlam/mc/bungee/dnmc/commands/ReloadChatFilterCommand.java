package com.ericlam.mc.bungee.dnmc.commands;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.dnmc.main.DragonNiteMC;
import com.ericlam.mc.bungee.dnmc.permission.Perm;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

public class ReloadChatFilterCommand extends CommandNode {


    public ReloadChatFilterCommand() {
        super(null, "reload-chat-filter", Perm.OWNER, "重載 chat-filter.yml", null, "reload-cf");
    }

    @Override
    public void executeCommand(CommandSender sender, List<String> args) {
        DragonNiteMC.getDnBungeeConfig().reloadChatFilter();
        MessageBuilder.sendMessage(sender, DragonNiteMC.getAPI().getMainConfig().getPrefix()+"§e重載成功");
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
