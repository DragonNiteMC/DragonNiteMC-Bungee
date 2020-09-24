package com.ericlam.mc.bungee.hnmc.commands.hnmcbs;

import com.ericlam.mc.bungee.hnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.hnmc.main.HyperNiteMC;
import com.ericlam.mc.bungee.hnmc.permission.Perm;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

public class UpdateChatFormatCommand extends CommandNode {

    public UpdateChatFormatCommand(CommandNode parent) {
        super(parent, "updateformat", Perm.ADMIN, "更新權限群組前後綴", null, "updatechatformat", "updatechat");
    }

    @Override
    public void executeCommand(CommandSender sender, List<String> args) {
        HyperNiteMC.getAPI().getChatFormatManager().updateChatformatTask().whenComplete((v,ex)->{
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            MessageBuilder.sendMessage(sender, HyperNiteMC.getAPI().getMainConfig().getPrefix()+"§a聊天格式更新成功。");
        });
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
