package com.example.util;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class TestRedisPing {
    @Test
    public void testConnection() {
        try (Jedis jedis = new Jedis("localhost")) {
            // jedis.auth("033."); // 密码
            System.out.println(jedis.ping());
            // Redis中的「命令 参数」<==>Java中的「jedis方法 参数」
            System.out.println(jedis.get("num"));
            jedis.set("num", "5");
            System.out.println(jedis.get("num"));
        }
    }
}