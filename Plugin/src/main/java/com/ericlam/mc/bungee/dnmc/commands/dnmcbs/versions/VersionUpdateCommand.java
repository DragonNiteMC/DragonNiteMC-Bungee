package com.ericlam.mc.bungee.dnmc.commands.dnmcbs.versions;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.dnmc.managers.ResourceManager;
import com.ericlam.mc.bungee.dnmc.permission.Perm;
import net.md_5.bungee.api.CommandSender;

public class VersionUpdateCommand extends VersionCommandNode {

    public VersionUpdateCommand(CommandNode parent) {
        super(parent, "update", Perm.DEVELOPER, "更新插件到最新版本", "<plugin>", "download");
    }

    @Override
    public void executeChecker(CommandSender sender, ResourceManager manager, String plugin, String version) {
        MessageBuilder.sendMessage(sender, config.getPrefix() + "§c暫不支援此功能。");
    }
}

