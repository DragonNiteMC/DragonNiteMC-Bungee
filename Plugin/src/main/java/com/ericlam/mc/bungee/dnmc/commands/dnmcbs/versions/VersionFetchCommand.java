package com.ericlam.mc.bungee.dnmc.commands.dnmcbs.versions;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.dnmc.managers.ResourceManager;
import com.ericlam.mc.bungee.dnmc.permission.Perm;
import net.md_5.bungee.api.CommandSender;

import java.io.IOException;

public class VersionFetchCommand extends VersionCommandNode {

    public VersionFetchCommand(CommandNode parent) {
        super(parent, "fetch", Perm.DEVELOPER, "刷新該插件的最新版本", "<plugin>", "get");
    }

    @Override
    public void executeChecker(CommandSender sender, ResourceManager manager, String plugin, String version) {
        MessageBuilder.sendMessage(sender, config.getPrefix() + "§e正在刷新插件 " + plugin + " 的最新版本...");
        manager.fetchLatestVersion(plugin, v -> {
            MessageBuilder.sendMessage(sender, config.getPrefix() + "§a 插件 " + plugin + " 的最新版本刷新完畢。 最新版本為 v" + v);
        }, err -> {
            MessageBuilder.sendMessage(sender, config.getPrefix() + "§c 插件 " + plugin + " 刷新最新版本時出現錯誤: " + err.getMessage());
            if (err instanceof IOException) err.printStackTrace();
        });
    }
}

