package com.ericlam.mc.bungee.dnmc;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public interface RedisDataSource {
    Jedis getJedis();

    JedisPool getJedisPool();
}
