package com.ericlam.mc.bungee.dnmc.managers;

import com.ericlam.mc.bungee.dnmc.config.ConfigFactory;
import com.ericlam.mc.bungee.dnmc.config.YamlManager;
import com.ericlam.mc.bungee.dnmc.config.yaml.BungeeConfiguration;
import com.ericlam.mc.bungee.dnmc.config.yaml.Resource;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ConfigBuilder implements ConfigFactory {

    private final Map<String, Class<? extends BungeeConfiguration>> ymls = new HashMap<>();
    private final Plugin plugin;

    public ConfigBuilder(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public ConfigFactory register(String yml, Class<? extends BungeeConfiguration> configClass) {
        this.ymls.put(yml, configClass);
        return this;
    }

    @Override
    public ConfigFactory register(Class<? extends BungeeConfiguration> configClass) {
        Resource res = configClass.getAnnotation(Resource.class);
        if (res == null) throw new IllegalStateException("缺少 @Resource 標註");
        this.ymls.put(res.locate(), configClass);
        return this;
    }


    @Override
    public YamlManager dump() {
        return new YamlHandler(ymls, plugin);
    }
}
