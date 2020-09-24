package com.ericlam.mc.bungee.hnmc.managers;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.CompletableFuture;

public interface ChatFormatManager {

    /**
     * @param player 玩家
     * @return 前綴和後綴, 若沒有則返回兩個空白
     */
    String[] getPrefixSuffix(ProxiedPlayer player);

    /**
     * @param player 玩家
     * @return 前綴, 若沒有則返回空白
     */
    String getPrefix(ProxiedPlayer player);

    /**
     * @param player 玩家
     * @return 後綴, 若沒有則返回空白
     */
    String getSuffix(ProxiedPlayer player);

    /**
     * 從 mysql 更新聊天格式
     *
     * @return 完成後
     */
    CompletableFuture<Void> updateChatformatTask();

}
