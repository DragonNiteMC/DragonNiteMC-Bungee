package com.ericlam.mc.bungee.hnmc.config;

import com.ericlam.mc.bungee.hnmc.config.yaml.MessageConfiguration;
import com.ericlam.mc.bungee.hnmc.config.yaml.Resource;

import java.util.List;
import java.util.Map;

@Resource(locate = "joinme.yml")
public class JoinMeConfig extends MessageConfiguration {

    public Map<String, List<String>> categories;

    public List<String> blacklistServers;

    public boolean onlyBroadcastLobbies;

    public List<String> lobbyServers;

    public String permissionUse;

    public int joinValidTime;

    public int globalCooldown;

    public String cmd;

    public List<String> alias;
}
