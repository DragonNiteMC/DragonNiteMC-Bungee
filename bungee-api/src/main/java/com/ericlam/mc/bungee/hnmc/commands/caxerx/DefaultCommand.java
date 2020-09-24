package com.ericlam.mc.bungee.hnmc.commands.caxerx;

import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * 父類指令預設器
 * <p>
 * 自動返回幫助訊息
 *
 * @author caxerx
 */
public class DefaultCommand extends CommandNode {

    /**
     * @param parent      父類指令
     * @param command     指令
     * @param permission  權限
     * @param description 介紹
     * @param alias       縮寫
     */
    public DefaultCommand(CommandNode parent, String command, String permission, String description, String... alias) {
        super(parent, command, permission, description, null, alias);
    }

    @Override
    public void executeCommand(CommandSender sender, List<String> args) {
        HelpOutputBuilder builder = new HelpOutputBuilder(this);
        getSubCommands().forEach(builder::append);
        sender.sendMessage(builder.build());
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        ArrayList<String> result = new ArrayList<>();
        getSubCommands().forEach(sub -> result.add(sub.getAlias().get(0)));
        return result;
    }
}
