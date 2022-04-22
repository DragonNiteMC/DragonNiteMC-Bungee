package com.ericlam.mc.bungee.dnmc.commands.dnmcbs.versions;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.dnmc.exceptions.PluginNotFoundException;
import com.ericlam.mc.bungee.dnmc.exceptions.ResourceNotFoundException;
import com.ericlam.mc.bungee.dnmc.managers.ResourceManager;
import com.ericlam.mc.bungee.dnmc.permission.Perm;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class VersionCheckCommand extends VersionCommandNode {


    public VersionCheckCommand(CommandNode parent) {
        super(parent, "check", Perm.DEVELOPER, "檢查該插件的版本是否最新", "<plugin>", "latest");
    }

    @Override
    public void executeChecker(CommandSender sender, ResourceManager manager, String plugin, String version) throws ResourceNotFoundException, PluginNotFoundException {
        var v = manager.getLatestVersion(plugin);
        var b = manager.isLatestVersion(plugin);
        MessageBuilder.sendMessage(sender, config.getPrefix() + (b ? ChatColor.GREEN : ChatColor.RED) + "插件 " + plugin + " 目前版本 v" + version + ", 最新版本為 v" + v + ", " + (b ? "沒有" : "有") + "可用的更新。");
    }
}

