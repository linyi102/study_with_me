package com.example.util;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestJedisPool {
    @Test
    public void testJedisPool() {
        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
        JedisPool jedisPool2 = JedisPoolUtil.getJedisPoolInstance();

        assert (jedisPool == jedisPool2); // 获取的是同一个池
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set("num", "111");
        }
    }
}