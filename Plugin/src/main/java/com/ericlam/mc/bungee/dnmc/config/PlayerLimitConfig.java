package com.ericlam.mc.bungee.dnmc.config;

import com.ericlam.mc.bungee.dnmc.config.yaml.MessageConfiguration;
import com.ericlam.mc.bungee.dnmc.config.yaml.Resource;

@Resource(locate = "player-limit.yml")
public class PlayerLimitConfig extends MessageConfiguration {
    public int maxPlayers;
}
