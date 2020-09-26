package com.ericlam.mc.bungee.hnmc.updater;

import com.ericlam.mc.bungee.hnmc.exceptions.PluginNotFoundException;
import com.ericlam.mc.bungee.hnmc.exceptions.ResourceNotFoundException;
import com.ericlam.mc.bungee.hnmc.listeners.VersionUpdateListener;
import com.ericlam.mc.bungee.hnmc.managers.ResourceManager;
import com.google.gson.Gson;
import com.google.inject.Inject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class HyperNiteResourceManager implements ResourceManager {

    private static final String WEB_API = "https://updater.chu77.xyz/plugin/";
    private static final Gson GSON = new Gson();

    private final Map<String, String> versionMap = new ConcurrentHashMap<>();

    @Inject
    private Plugin bungeePlugin;

    @Override
    public String getLatestVersion(String plugin) throws PluginNotFoundException, ResourceNotFoundException {
        validateResource(plugin);
        return Optional.ofNullable(versionMap.get(plugin)).orElseThrow(() -> new ResourceNotFoundException(plugin));
    }

    @Override
    public boolean isLatestVersion(String plugin) throws PluginNotFoundException, ResourceNotFoundException {
        var resource = validateResource(plugin);
        return VersionUpdateListener.versionNewer(resource.getDescription().getVersion(), getLatestVersion(plugin));
    }

    @Override
    public void fetchLatestVersion(String plugin, Consumer<String> afterRun, Consumer<Exception> errorRun) {
        ProxyServer.getInstance().getScheduler().runAsync(bungeePlugin, new HyperNitePluginUpdate(plugin, afterRun, errorRun));
    }

    @Override
    public CompletableFuture<File> downloadLatestVersion(String plugin) {
        throw new UnsupportedOperationException("目前暫不支援下載。");
    }

    private Plugin validateResource(String plugin) throws PluginNotFoundException {
        return Optional.ofNullable(bungeePlugin.getProxy().getPluginManager().getPlugin(plugin)).orElseThrow(() -> new PluginNotFoundException(plugin));
    }

    @SuppressWarnings("unchecked")
    private class HyperNitePluginUpdate extends PluginUpdateRunnable {

        protected HyperNitePluginUpdate(String plugin, Consumer<String> afterRun, Consumer<Exception> errorRun) {
            super(plugin, afterRun, errorRun);
        }

        @Override
        public String execute(String plugin) throws Exception {
            URL url = new URL(WEB_API.concat(plugin));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            if (con.getResponseCode() == 404) {
                throw new ResourceNotFoundException(plugin);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            var map = (Map<String, String>) GSON.fromJson(in, Map.class);
            var v = map.get("version");
            versionMap.put(plugin, v);
            return v;
        }
    }
}

