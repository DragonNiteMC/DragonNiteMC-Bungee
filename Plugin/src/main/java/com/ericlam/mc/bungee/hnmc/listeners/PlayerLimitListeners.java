package com.ericlam.mc.bungee.hnmc.listeners;

import com.ericlam.mc.bungee.hnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.hnmc.config.PlayerLimitConfig;
import com.ericlam.mc.bungee.hnmc.main.HyperNiteMC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class PlayerLimitListeners implements Listener {
    private final String[] reject_msgs;
    private final int maxAllowed;

    public PlayerLimitListeners() {
        PlayerLimitConfig limit = HyperNiteMC.getHnBungeeConfig().getLimit();
        reject_msgs = limit.getList("reject-msg").toArray(String[]::new);
        maxAllowed = limit.maxPlayers;
    }

    @EventHandler
    public void onPlayerLogin(final PostLoginEvent e) {
        int total = ProxyServer.getInstance().getOnlineCount();
        ProxiedPlayer player = e.getPlayer();
        if (total <= maxAllowed) return;
        //if (player.getPendingConnection().isOnlineMode()) return;
        //player.getPendingConnection().disconnect(new MessageBuilder(reject_msgs).build());

        HyperNiteMC.getAPI().getPlayerManager().getOfflinePlayer(player.getUniqueId()).whenComplete((offlinePlayer, throwable) -> {
            if (throwable != null){
                throwable.printStackTrace();
                return;
            }

            offlinePlayer.ifPresent(p->{
                if (p.isPremium()) return;
                player.disconnect(new MessageBuilder(reject_msgs).build());
            });
        });
    }

    @EventHandler
    public void onPlayerChat(final ChatEvent e){
        List<String> filterList = HyperNiteMC.getHnBungeeConfig().getFilter().filterList;
        String messages = e.getMessage();
        if (e.isCommand()) return;
        for (String word : filterList) {
            messages = messages.replaceAll(word,"*".repeat(word.length()));
        }
        e.setMessage(messages);
    }
}
