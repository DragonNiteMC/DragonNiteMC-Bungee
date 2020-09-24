package com.ericlam.mc.bungee.hnmc.commands;

import com.ericlam.mc.bungee.hnmc.main.HNBungeeConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PingCommand extends Command {


    public PingCommand() {
        super("ping");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(TextComponent.fromLegacyText(HNBungeeConfig.langConfig.get("not-player")));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        if (strings.length < 1) {
            player.sendMessage(TextComponent.fromLegacyText(HNBungeeConfig.langConfig.get("ping").replaceAll("<ping>", player.getPing() + "")));
        } else {
            if (!player.hasPermission("group.helper")) {
                player.sendMessage(TextComponent.fromLegacyText(HNBungeeConfig.langConfig.get("no-perm")));
                return;
            }
            String targetStr = strings[0];
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetStr);
            if (target == null) {
                player.sendMessage(TextComponent.fromLegacyText(HNBungeeConfig.langConfig.get("no-this-player")));
                return;
            }
            player.sendMessage(TextComponent.fromLegacyText(HNBungeeConfig.langConfig.get("ping-other").replaceAll("<player>", targetStr).replaceAll("<ping>", target.getPing() + "")));
        }
    }
}
