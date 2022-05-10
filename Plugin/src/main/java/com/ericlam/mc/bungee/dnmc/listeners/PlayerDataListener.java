package com.ericlam.mc.bungee.dnmc.listeners;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.container.OfflineData;
import com.ericlam.mc.bungee.dnmc.events.PlayerVerifyCompletedEvent;
import com.ericlam.mc.bungee.dnmc.main.DragoniteMC;
import com.ericlam.mc.bungee.dnmc.managers.OfflinePlayerManager;
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
        e.registerIntent(DragoniteMC.plugin);
        PendingConnection connection = e.getConnection();
        String name = connection.getName();
        ProxyServer.getInstance().getScheduler().runAsync(DragoniteMC.plugin,()->{
            Optional<OfflineData> offlineData = offlinePlayerManager.getPlayerDataOrRequest(name);

            if (offlineData.isEmpty()){
                e.setCancelled(true);
                e.setCancelReason(new MessageBuilder("&c玩家資料獲取失敗", "&e請稍候再嘗試進入。").build());
                e.completeIntent(DragoniteMC.plugin);
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
            e.completeIntent(DragoniteMC.plugin);
        });
    }

    @EventHandler
    public void onSkinApply(final LoginEvent e) {
        e.registerIntent(DragoniteMC.plugin);
        DragoniteMC.getAPI().getSkinValueManager().getOrSaveSkinForPlayer(e.getConnection().getUniqueId()).whenComplete((skin, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                e.completeIntent(DragoniteMC.plugin);
                return;
            }
            final LoginResult result = DragoniteMC.getAPI().getSkinValueManager().editResult(e.getLoginResult(), skin);
            e.setLoginResult(result);
            e.completeIntent(DragoniteMC.plugin);
        });
    }
}
