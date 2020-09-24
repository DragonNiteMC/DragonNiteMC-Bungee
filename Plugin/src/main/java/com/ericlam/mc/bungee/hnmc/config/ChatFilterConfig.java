package com.ericlam.mc.bungee.hnmc.config;

import com.ericlam.mc.bungee.hnmc.config.yaml.BungeeConfiguration;
import com.ericlam.mc.bungee.hnmc.config.yaml.Resource;

import java.util.List;

@Resource(locate = "chat-filter.yml")
public class ChatFilterConfig extends BungeeConfiguration {
    public List<String> filterList;
}
