package com.ericlam.mc.bungee.hnmc.config;

import com.ericlam.mc.bungee.hnmc.config.yaml.BungeeConfiguration;
import com.ericlam.mc.bungee.hnmc.config.yaml.Resource;

import java.util.Map;

@Resource(locate = "version-checker.yml")
public class VersionCheckerConfig extends BungeeConfiguration {

    public long intervalHours;

    public boolean enabled_spigot_check;

    public Map<String, Long> resourceId_to_checks;

    public boolean ignore_unknown;

    public boolean use_unequal_check;

}
