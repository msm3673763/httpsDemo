package com.masm.distribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by masiming on 2017/11/18 10:04.
 */
public class RedisDistributedLock {

    @Autowired
    JedisPool jedisPool;

    private static final String LOCK_NODE = "LOCK";
    private ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public Boolean lock() {
        Jedis jedis = jedisPool.getResource();
        String value = UUID.randomUUID().toString();
        try {
            String ret = jedis.set(LOCK_NODE, value, "NX", "PX", 10000);
            if (!StringUtils.isEmpty(ret) && "OK".equals(ret)) {
                threadLocal.set(value);
                return true;
            }
            return false;
        } finally {
            jedis.close();
        }
    }

    public void nuLock() throws IOException {
        Jedis jedis = jedisPool.getResource();
        String script = null;
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("classpath:unLock.lua");
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] bytes = new byte[1024];
            while (-1 != bis.read(bytes)) {
                script = new String(bytes);
            }
            List<String> keys = new ArrayList<>();
            keys.add(LOCK_NODE);
            List<String> args = new ArrayList<>();
            args.add(threadLocal.get());
            jedis.eval(script, keys, args);
        } finally {
            jedis.close();
        }
    }
}
