package com.ericlam.mc.bungee.dnmc.config;

import com.ericlam.mc.bungee.dnmc.config.yaml.BungeeConfiguration;
import com.ericlam.mc.bungee.dnmc.config.yaml.Resource;

@Resource(locate = "database.yml")
public class DatabaseConfig extends BungeeConfiguration {

    public String host;
    public int port;
    public String database;
    public String username;
    public String password;
    public boolean useSSL;
    public Pool Pool;
    public Redis Redis;

    public static class Pool{
        public String name;
        public int minSize;
        public int maxSize;
        public long connectionTimeout;
        public long idleTimeout;
        public long maxLifeTime;
    }

    public static class Redis{
        public boolean enabled;
        public String ip;
        public int port;
        public int timeout;
        public boolean usePassword;
        public String password;
    }
}
