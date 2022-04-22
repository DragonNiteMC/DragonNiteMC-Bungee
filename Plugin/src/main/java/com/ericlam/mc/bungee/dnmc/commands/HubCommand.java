package com.ericlam.mc.bungee.dnmc.commands;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.listeners.AvoidKickListeners;
import com.ericlam.mc.bungee.dnmc.main.DNBungeeConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;
import java.util.Map;

public class HubCommand extends Command {

    public HubCommand() {
        super("hub", "", "lobby", "back");
    }

    private ServerInfo findHub(ServerInfo local) {
        Map<String, List<String>> fallbacks = AvoidKickListeners.getFallbackList();
        for (String lobby : fallbacks.keySet()) {
            if (fallbacks.get(lobby).contains(local.getName())) {
                return ProxyServer.getInstance().getServerInfo(lobby);
            }
        }
        return null;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ProxiedPlayer target;
        if (strings.length < 1) {
            if (!(commandSender instanceof ProxiedPlayer)) {
                MessageBuilder.sendMessage(commandSender, DNBungeeConfig.langConfig.get("not-player"));
                return;
            }
            target = (ProxiedPlayer) commandSender;
        } else {
            String targetStr = strings[0];
            if (targetStr.equals("*ALL*")) {
                if (!(commandSender instanceof ProxiedPlayer)) {
                    MessageBuilder.sendMessage(commandSender, DNBungeeConfig.langConfig.get("not-player"));
                    return;
                }
                ProxiedPlayer player = (ProxiedPlayer) commandSender;
                ServerInfo hub = findHub(player.getServer().getInfo());
                if (hub == null) {
                    MessageBuilder.sendMessage(player, DNBungeeConfig.langConfig.get("no-hub"));
                    return;
                }
                for (ProxiedPlayer hubPlayer : hub.getPlayers()) {
                    MessageBuilder.sendMessage(hubPlayer, DNBungeeConfig.langConfig.get("send-hub"));
                    hubPlayer.connect(hub);
                }
                MessageBuilder.sendMessage(player, DNBungeeConfig.langConfig.get("send-hub-all"));
                return;
            }
            target = ProxyServer.getInstance().getPlayer(targetStr);
        }
        if (target == null) {
            MessageBuilder.sendMessage(commandSender, DNBungeeConfig.langConfig.get("no-this-player"));
            return;
        }
        ServerInfo hub = findHub(target.getServer().getInfo());
        if (hub == null) {
            MessageBuilder.sendMessage(commandSender, DNBungeeConfig.langConfig.get("no-hub"));
            return;
        }
        target.connect(hub);
        MessageBuilder.sendMessage(target, DNBungeeConfig.langConfig.get("send-hub"));
        if (target != commandSender){
            MessageBuilder.sendMessage(commandSender, DNBungeeConfig.langConfig.get("send-hub-player").replace("<player>", target.getDisplayName()));
        }
    }
}
