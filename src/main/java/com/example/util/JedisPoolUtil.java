package com.example.util;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil {
    // volatile，这样在引用对象时，避免可能发生的指令重排
    private static volatile JedisPool jedisPool = null;

    private JedisPoolUtil() {}

    public static JedisPool getJedisPoolInstance() {
        if (jedisPool == null) {
            synchronized (JedisPoolUtil.class) {
                if (jedisPool == null) {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxTotal(50); // 控制一个pool可分配多少个jedis实例
                    poolConfig.setMaxIdle(5); // 控制一个pool最多有多少个状态为idle(空闲)的jedis实例
                    poolConfig.setMaxWaitMillis(100 * 1000); // 表示当borrow一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛JedisConnectionException
                    poolConfig.setTestOnBorrow(true); // 获得一个jedis实例的时候是否检查连接可用性（ping()）；如果为true，则得到的jedis实例均是可用的

                    jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379);
                }
            }
        }
        return jedisPool;
    }
}