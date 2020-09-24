package com.ericlam.mc.bungee.hnmc.listeners;

import com.ericlam.mc.bungee.hnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.hnmc.container.OfflineData;
import com.ericlam.mc.bungee.hnmc.events.PlayerVerifyCompletedEvent;
import com.ericlam.mc.bungee.hnmc.main.HyperNiteMC;
import com.ericlam.mc.bungee.hnmc.managers.OfflinePlayerManager;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;

import java.time.Instant;
import java.util.Optional;

public class PlayerDataListener implements Listener {

    private final OfflinePlayerManager offlinePlayerManager;

    public PlayerDataListener(OfflinePlayerManager offlinePlayerManager) {
        this.offlinePlayerManager = offlinePlayerManager;
    }

    @EventHandler
    public void onPlayerLogin(final PreLoginEvent e) {
        e.registerIntent(HyperNiteMC.plugin);
        PendingConnection connection = e.getConnection();
        String name = connection.getName();
        ProxyServer.getInstance().getScheduler().runAsync(HyperNiteMC.plugin,()->{
            Optional<OfflineData> offlineData = offlinePlayerManager.getPlayerDataOrRequest(name);

            if (offlineData.isEmpty()){
                e.setCancelled(true);
                e.setCancelReason(new MessageBuilder("&c玩家資料獲取失敗", "&e請稍候再嘗試進入。").build());
                e.completeIntent(HyperNiteMC.plugin);
                return;
            }

            OfflineData data = offlineData.get();
            connection.setOnlineMode(data.isPremium());
            if (!data.isPremium()) {
                connection.setUniqueId(data.getUniqueId());
            } else if (!name.equals(data.getName())) {
                data.setName(name);
            }
            data.setLastLogin(Instant.now().toEpochMilli());
            Callback<PlayerVerifyCompletedEvent> callback = (ex, throwable1) -> offlinePlayerManager.saveToSQL(ex.getOfflinePlayer());
            ProxyServer.getInstance().getPluginManager().callEvent(new PlayerVerifyCompletedEvent(data, callback));
            e.completeIntent(HyperNiteMC.plugin);
        });
    }

    @EventHandler
    public void onSkinApply(final LoginEvent e) {
        e.registerIntent(HyperNiteMC.plugin);
        HyperNiteMC.getAPI().getSkinValueManager().getOrSaveSkinForPlayer(e.getConnection().getUniqueId()).whenComplete((skin, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                e.completeIntent(HyperNiteMC.plugin);
                return;
            }
            final LoginResult result = HyperNiteMC.getAPI().getSkinValueManager().editResult(e.getLoginResult(), skin);
            e.setLoginResult(result);
            e.completeIntent(HyperNiteMC.plugin);
        });
    }
}
