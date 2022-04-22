package com.ericlam.mc.bungee.dnmc.exceptions;

public class PluginNotFoundException extends Exception{
    private final String plugin;

    public PluginNotFoundException(String plugin) {
        super("找不到插件 "+plugin);
        this.plugin = plugin;
    }

    public String getPlugin() {
        return plugin;
    }
}
