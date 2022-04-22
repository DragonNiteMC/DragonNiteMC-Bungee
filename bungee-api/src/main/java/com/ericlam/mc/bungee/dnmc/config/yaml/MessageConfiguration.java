package com.ericlam.mc.bungee.dnmc.config.yaml;


import java.util.List;

/**
 * 用於創建 訊息類別 config 時繼承
 */
public abstract class MessageConfiguration extends BungeeConfiguration {

    private MessageGetter getter;

    /**
     * 若果 訊息類別 config 本身沒有 @Prefix, 返回的將是 null
     *
     * @return 前綴
     */
    public String getPrefix() {
        return getter.getPrefix();
    }

    public String getPure(String path) {
        return getter.getPure(path);
    }

    public String get(String path) {
        return getter.get(path);
    }

    public List<String> getList(String path) {
        return getter.getList(path);
    }

    public List<String> getPureList(String path) {
        return getter.getPureList(path);
    }
}
