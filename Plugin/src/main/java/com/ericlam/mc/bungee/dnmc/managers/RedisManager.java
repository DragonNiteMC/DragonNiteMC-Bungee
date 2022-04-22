package com.ericlam.mc.bungee.dnmc.managers;

import com.ericlam.mc.bungee.dnmc.RedisDataSource;
import com.ericlam.mc.bungee.dnmc.config.DatabaseConfig;
import com.ericlam.mc.bungee.dnmc.config.MainConfig;
import com.ericlam.mc.bungee.dnmc.main.DNBungeeConfig;
import com.google.inject.Inject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager implements RedisDataSource {

    private final JedisPool jedisPool;

    @Inject
    public RedisManager(MainConfig mainConfig) {
        DatabaseConfig.Redis info = ((DNBungeeConfig) mainConfig).getDatabaseConfig().Redis;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(30);
        config.setMaxTotal(100);
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(false);
        config.setTestOnBorrow(false);
        config.setTestWhileIdle(true);
        if (info.usePassword) {
            jedisPool = new JedisPool(config, info.ip, info.port, info.timeout * 1000, info.password);
        } else {
            jedisPool = new JedisPool(config, info.ip, info.port, info.timeout * 1000);
        }
    }

    @Override
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    @Override
    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
