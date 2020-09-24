package com.ericlam.mc.bungee.hnmc.managers;

import com.ericlam.mc.bungee.hnmc.container.OfflinePlayer;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerManager {

    /**
     * @param uuid UUID
     * @return 可能為 null 的 OfflinePlayer
     */
    CompletableFuture<Optional<OfflinePlayer>> getOfflinePlayer(UUID uuid);

    /**
     * @param name 玩家名稱
     * @return 可能為 null 的 OfflinePlayer
     */
    CompletableFuture<Optional<OfflinePlayer>> getOfflinePlayer(String name);

}
