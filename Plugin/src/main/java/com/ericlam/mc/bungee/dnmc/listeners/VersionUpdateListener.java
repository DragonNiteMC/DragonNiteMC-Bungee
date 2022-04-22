package com.ericlam.mc.bungee.dnmc.listeners;

import com.ericlam.mc.bungee.dnmc.config.VersionCheckerConfig;
import com.ericlam.mc.bungee.dnmc.exceptions.ResourceNotFoundException;
import com.ericlam.mc.bungee.dnmc.main.DragonNiteMC;
import com.ericlam.mc.bungee.dnmc.managers.ResourceManager;
import com.ericlam.mc.bungee.dnmc.permission.Perm;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class VersionUpdateListener implements Listener {

    private final Map<String, String> updateMap = new ConcurrentHashMap<>();

    public VersionUpdateListener(DragonNiteMC dragonNiteMC) {
        ProxyServer.getInstance().getScheduler().schedule(dragonNiteMC, new CheckerRunnable(dragonNiteMC), DragonNiteMC.getDnBungeeConfig().getVersionChecker().intervalHours, TimeUnit.HOURS);
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent e) {
        if (!e.getPlayer().hasPermission(Perm.DEVELOPER)) return;
        updateMap.forEach((plugin, versions)->{
            var version = versions.split(":");
            e.getPlayer().sendMessage(DragonNiteMC.getDnBungeeConfig().getPrefix() + "§c 插件更新: " + plugin + " v" + version[1] + ", 最新版本: v" + version[0]);
        });
    }


    private class CheckerRunnable implements Runnable {

        private final VersionCheckerConfig config;
        private final DragonNiteMC api;

        public CheckerRunnable(DragonNiteMC dragonNiteMC) {
            this.config = DragonNiteMC.getDnBungeeConfig().getVersionChecker();
            this.api = dragonNiteMC;
        }

        @Override
        public void run() {
            for (Plugin resource : ProxyServer.getInstance().getPluginManager().getPlugins()) {
                String plugin = resource.getDescription().getName();
                if (config.resourceId_to_checks.containsKey(plugin) && config.enabled_spigot_check) {
                    api.getResourceManager(ResourceManager.Type.SPIGOT).fetchLatestVersion(plugin, v -> {
                        if (versionNewer(resource.getDescription().getVersion(), v)){
                            updateMap.put(plugin, v+":"+resource.getDescription().getVersion());
                        }
                    }, err -> {
                        api.getLogger().warning("獲取插件 " + plugin + " 的最新版本時出現錯誤: " + err.getMessage() + " (插件資源 id 可能輸入有誤。)");
                        if (err instanceof IOException) err.printStackTrace();
                    });
                } else if (!config.resourceId_to_checks.containsKey(plugin)) {
                    api.getResourceManager(ResourceManager.Type.DRAGONNITE).fetchLatestVersion(plugin, v -> {
                        if (versionNewer(resource.getDescription().getVersion(), v)){
                            updateMap.put(plugin, v+":"+resource.getDescription().getVersion());
                        }
                    }, err -> {
                        if (err instanceof ResourceNotFoundException && config.ignore_unknown) return;
                        api.getLogger().warning("獲取插件 " + plugin + " 的最新版本時出現錯誤: " + err.getMessage());
                        if (err instanceof IOException) err.printStackTrace();
                    });
                }
            }
        }
    }

    private static final Pattern pt = Pattern.compile("(^[\\d\\.]+)");

    public static boolean versionNewer(String versionCurrent, String versionLatest) {
        var unequal = DragonNiteMC.getDnBungeeConfig().getVersionChecker().use_unequal_check;
        if (unequal) return versionCurrent.equals(versionLatest);
        else {
            if (versionCurrent.equals(versionLatest)) return true;
            var currentMatcher = pt.matcher(versionCurrent);
            var latestMatcher = pt.matcher(versionLatest);
            String[] current;
            String[] latest;
            if (currentMatcher.find()){
                current = currentMatcher.group().split("\\.");
            }else{
                return false;
            }
            if (latestMatcher.find()){
                latest = latestMatcher.group().split("\\.");
            }else{
                return true;
            }
            int length = Math.max(current.length, latest.length);
            for (int i = 0; i < length; i++) {
                int currentNum = i < current.length ? Integer.parseInt(current[i]) : 0;
                int latestNum = i < latest.length ? Integer.parseInt(latest[i]) : 0;
                if (currentNum > latestNum) return true;
                else if (currentNum < latestNum) return false;
            }
            return true;
        }
    }
}

