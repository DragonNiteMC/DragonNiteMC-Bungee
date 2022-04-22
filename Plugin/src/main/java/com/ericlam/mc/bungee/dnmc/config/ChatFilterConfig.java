package com.ericlam.mc.bungee.dnmc.config;

import com.ericlam.mc.bungee.dnmc.config.yaml.BungeeConfiguration;
import com.ericlam.mc.bungee.dnmc.config.yaml.Resource;

import java.util.List;

@Resource(locate = "chat-filter.yml")
public class ChatFilterConfig extends BungeeConfiguration {
    public List<String> filterList;
}
