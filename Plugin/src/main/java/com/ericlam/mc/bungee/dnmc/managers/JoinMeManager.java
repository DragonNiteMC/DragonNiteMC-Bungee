package com.ericlam.mc.bungee.dnmc.managers;

import com.ericlam.mc.bungee.dnmc.config.MainConfig;
import com.ericlam.mc.bungee.dnmc.main.DNBungeeConfig;
import com.ericlam.mc.bungee.dnmc.main.DragonNiteMC;
import com.google.inject.Inject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JoinMeManager {

    private final HashMap<String, ServerInfo> joinme = new HashMap<>();
    private final HashSet<UUID> cooldown = new HashSet<>();
    private boolean globalCooling = false;

    private final long joinmeValidTime;
    private final long joinmeGlobalCooldown;

    @Inject
    public JoinMeManager(MainConfig config) {
        DNBungeeConfig bungeeConfig = ((DNBungeeConfig) config);
        this.joinmeGlobalCooldown = bungeeConfig.getJoinMeConfig().globalCooldown;
        this.joinmeValidTime = bungeeConfig.getJoinMeConfig().joinValidTime;
    }

    public void addCooldown(UUID uuid) {
        if (cooldown.add(uuid))
            ProxyServer.getInstance().getScheduler().schedule(DragonNiteMC.plugin, () -> cooldown.remove(uuid), (long) (joinmeGlobalCooldown * 1.5), TimeUnit.SECONDS);
    }

    public boolean isCooldown(UUID uuid) {
        return cooldown.contains(uuid);
    }

    public boolean isGlobalCooling() {
        return globalCooling;
    }

    private void setGlobalCooling(boolean globalCooling) {
        this.globalCooling = globalCooling;
    }

    public void addJoinMe(UUID uuid, ServerInfo serverInfo) {
        String uuidStr = uuid.toString().replaceAll("-", "");
        if (joinme.putIfAbsent(uuidStr, serverInfo) == null) {
            setGlobalCooling(true);
            ProxyServer.getInstance().getScheduler().schedule(DragonNiteMC.plugin, () -> joinme.remove(uuidStr), joinmeValidTime, TimeUnit.SECONDS);
            ProxyServer.getInstance().getScheduler().schedule(DragonNiteMC.plugin, () -> setGlobalCooling(false), joinmeGlobalCooldown, TimeUnit.SECONDS);
        }
    }

    public boolean uuidExist(UUID uuid) {
        return joinme.containsKey(uuid.toString().replaceAll("-", ""));
    }

    public ServerInfo getServerInfo(String uuid) {
        return joinme.get(uuid);
    }
}
