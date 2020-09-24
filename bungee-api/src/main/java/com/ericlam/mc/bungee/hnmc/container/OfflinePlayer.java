package com.ericlam.mc.bungee.hnmc.container;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public interface OfflinePlayer {
    /**
     * @return 離線玩家名字
     */
    String getName();

    /**
     * @return 離線玩家UUID
     */
    UUID getUniqueId();

    /**
     * @return 是否正版玩家
     */
    boolean isPremium();

    /**
     * @return 上次登入 timestamp
     */
    long lastLogin();

    /**
     * @return 在線
     */
    boolean isOnline();


    /**
     * @return 在線玩家
     */
    ProxiedPlayer getPlayer();

}
