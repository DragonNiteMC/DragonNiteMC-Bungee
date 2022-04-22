package com.ericlam.mc.bungee.dnmc.listeners;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.config.AvoidKickConfig;
import com.ericlam.mc.bungee.dnmc.main.DragonNiteMC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvoidKickListeners implements Listener {
    private final AvoidKickConfig avoid;
    private static Map<String, List<String>> fallbackList = new HashMap<>();

    public static Map<String, List<String>> getFallbackList() {
        return fallbackList;
    }

    public AvoidKickListeners() {
        avoid = DragonNiteMC.getDnBungeeConfig().getAvoid_back();
        fallbackList = avoid.customFallBack;
    }

    @EventHandler
    public void onPlayerKick(final ServerKickEvent e) {
        if (e.getState() != ServerKickEvent.State.CONNECTED) return;
        ProxiedPlayer player = e.getPlayer();
        ServerInfo kickFrom = e.getKickedFrom();
        ServerInfo cancel = e.getCancelServer();
        if (cancel == null) return;
        boolean avoid_kick = false;
        main:
        for (BaseComponent component : e.getKickReasonComponent()) {
            String line = component.toLegacyText().replaceAll("§[a-z0-9A-Z]", "");
            if (!this.avoid.useAsWhitelist) {
                for (String reasons : avoid.reasons) {
                    if (line.contains(reasons)) {
                        avoid_kick = true;
                        break main;
                    }
                }
            } else {
                boolean pass = true;
                for (String reasons : avoid.reasons) {
                    pass = pass && !line.contains(reasons);
                }
                if (pass) {
                    avoid_kick = true;
                    break;
                }
            }
        }
        if (!avoid_kick) return;
        e.setCancelled(true);
        ServerInfo fallbackServer = cancel;
        if (!avoid.useCancelServer) {
            for (String lobby : fallbackList.keySet()) {
                if (fallbackList.get(lobby).contains(kickFrom.getName())) {
                    fallbackServer = ProxyServer.getInstance().getServerInfo(lobby);
                    break;
                }
            }
        }
        if (fallbackServer == null) fallbackServer = cancel;
        e.setCancelServer(fallbackServer);

        String[] msgs = avoid.getList("connect-msg").stream().map(line -> ChatColor.translateAlternateColorCodes('&', line.replaceAll("<kick-server>", kickFrom.getName()))).toArray(String[]::new);
        player.sendMessage(new MessageBuilder(msgs).build());
        player.sendMessage(e.getKickReasonComponent());

    }
}
