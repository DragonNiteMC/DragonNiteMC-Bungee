package com.ericlam.mc.bungee.hnmc.listeners;

import com.ericlam.mc.bungee.hnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.hnmc.config.JoinMeConfig;
import com.ericlam.mc.bungee.hnmc.config.LangConfig;
import com.ericlam.mc.bungee.hnmc.config.MainConfig;
import com.ericlam.mc.bungee.hnmc.main.HNBungeeConfig;
import com.ericlam.mc.bungee.hnmc.managers.JoinMeManager;
import com.google.inject.Inject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class JoinServerListeners implements Listener {

    @Inject
    private JoinMeManager joinMeManager;

    private final JoinMeConfig joinMeConfig;

    @Inject
    public JoinServerListeners(MainConfig config){
        HNBungeeConfig bungeeConfig = ((HNBungeeConfig) config);
        this.joinMeConfig = bungeeConfig.getJoinMeConfig();
    }

    @EventHandler
    public void onTextClick(final ChatEvent e){
        if (!(e.getSender() instanceof ProxiedPlayer)) return;
        ProxiedPlayer accpeter = (ProxiedPlayer) e.getSender();
        String[] msglist = e.getMessage().split("_");
        if (msglist.length!=2) return;
        String uid = msglist[0];
        String inviterUUID = msglist[1];
        ProxiedPlayer inviter;
        if (inviterUUID.length() != 36) return;
        if (uid.length() != 32) return;

        try{
            inviter = ProxyServer.getInstance().getPlayer(UUID.fromString(inviterUUID));
        } catch (IllegalArgumentException ex){
            return;
        }

        e.setCancelled(true);

        ServerInfo info = joinMeManager.getServerInfo(uid);

        if (info == null) {
            MessageBuilder.sendMessage(accpeter, joinMeConfig.get("invalid"));
            return;
        }

        if (!accpeter.hasPermission("crystal.premium")) {
            MessageBuilder.sendMessage(accpeter, HNBungeeConfig.langConfig.get("only-premium"));
            MessageBuilder.sendMessage(accpeter, joinMeConfig.get("fail-join"));
            return;
        }

        if (!inviter.getServer().getInfo().equals(info)){
            MessageBuilder.sendMessage(accpeter, joinMeConfig.get("player-left-server"));
            MessageBuilder.sendMessage(accpeter, joinMeConfig.get("fail-join"));
            return;
        }
        if (accpeter.getServer().getInfo().equals(info)){
            MessageBuilder.sendMessage(accpeter, joinMeConfig.get("already-there"));
            MessageBuilder.sendMessage(accpeter, joinMeConfig.get("fail-join"));
            return;
        }

        accpeter.connect(info);
        MessageBuilder.sendMessage(accpeter, joinMeConfig.get("success-join").replaceAll("<player>", inviter.getDisplayName()));
        MessageBuilder.sendMessage(inviter, joinMeConfig.get("join-msg").replaceAll("<player>", accpeter.getDisplayName()));

    }
}
