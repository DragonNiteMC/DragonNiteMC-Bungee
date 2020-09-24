package com.ericlam.mc.bungee.hnmc.config;

import com.ericlam.mc.bungee.hnmc.config.yaml.MessageConfiguration;
import com.ericlam.mc.bungee.hnmc.config.yaml.Resource;

@Resource(locate = "player-limit.yml")
public class PlayerLimitConfig extends MessageConfiguration {
    public int maxPlayers;
}
