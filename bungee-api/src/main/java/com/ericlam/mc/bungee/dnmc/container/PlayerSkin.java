package com.ericlam.mc.bungee.dnmc.container;

public interface PlayerSkin {
    /**
     * @return 皮膚 base64
     */
    String getValue();

    /**
     * @return 上次更新時間
     */
    long getTimestamp();

    /**
     * @return 是否為正版皮膚
     */
    boolean isPremium();

    /**
     * @return base64 代碼
     */
    String getSignature();
}
