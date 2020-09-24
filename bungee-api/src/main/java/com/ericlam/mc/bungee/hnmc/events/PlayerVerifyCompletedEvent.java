package com.ericlam.mc.bungee.hnmc.events;

import com.ericlam.mc.bungee.hnmc.container.OfflinePlayer;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.event.AsyncEvent;

/**
 * 對玩家進行離線數據提取完成後的事件
 *
 * Async Event
 *
 * <p>
 * 此事件在保存至資料庫的步驟前，因此修改離線數據會影響到資料庫數據的存儲。
 */
public class PlayerVerifyCompletedEvent extends AsyncEvent<PlayerVerifyCompletedEvent> {

    private OfflinePlayer offlinePlayer;

    public PlayerVerifyCompletedEvent(OfflinePlayer player, Callback<PlayerVerifyCompletedEvent> done) {
        super(done);
        this.offlinePlayer = player;
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }


    public void setOfflinePlayer(OfflinePlayer offlinePlayer) {
        this.offlinePlayer = offlinePlayer;
    }
}
