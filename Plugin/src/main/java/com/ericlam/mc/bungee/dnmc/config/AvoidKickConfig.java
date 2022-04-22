package com.ericlam.mc.bungee.dnmc.config;

import com.ericlam.mc.bungee.dnmc.config.yaml.MessageConfiguration;
import com.ericlam.mc.bungee.dnmc.config.yaml.Resource;

import java.util.List;
import java.util.Map;

@Resource(locate = "avoid-kick.yml")
public class AvoidKickConfig extends MessageConfiguration {
    public List<String> reasons;
    public boolean useAsWhitelist;
    public boolean useCancelServer;
    public Map<String, List<String>> customFallBack;
}
