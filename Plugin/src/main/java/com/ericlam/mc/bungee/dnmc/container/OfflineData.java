package com.ericlam.mc.bungee.dnmc.container;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class OfflineData implements OfflinePlayer {


    private String name;

    private UUID uniqueId;

    private boolean premium;

    private long lastLogin;


    public OfflineData(String name, UUID uniqueId, boolean premium, long lastLogin) {
        this.name = name;
        this.uniqueId = uniqueId;
        this.premium = premium;
        this.lastLogin = lastLogin;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public boolean isPremium() {
        return premium;
    }

    @Override
    public long lastLogin() {
        return lastLogin;
    }

    @Override
    public boolean isOnline() {
        return ProxyServer.getInstance().getPlayer(uniqueId) != null || ProxyServer.getInstance().getPlayer(name) != null;
    }

    @Override
    public ProxiedPlayer getPlayer() {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
        if (player == null) player = ProxyServer.getInstance().getPlayer(name);
        return player;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean equalName(String name){
        return this.name.equals(name);
    }


    public boolean equalUUID(UUID uuid){
        return this.uniqueId.equals(uuid);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) return false;
        OfflineData data = (OfflineData) obj;
        return equalName(data.getName()) && equalUUID(data.getUniqueId());
    }

    @Override
    public int hashCode() {
        return this.getUniqueId().hashCode() + this.getName().hashCode();
    }
}
