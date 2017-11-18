package com.masm.cache.lock.jedis;

import com.masm.cache.lock.DistributedLock;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by masiming on 2017/11/18 10:04.
 * 不能在redis集群环境中使用（集群环境需要用到redLock）
 */
public class JedisDistributedLock implements DistributedLock {

    @Autowired
    JedisPool jedisPool;

    private static final String LOCK_NODE = "LOCK";
    private ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Override
    public void lock(long waitTime, TimeUnit timeUnit) {
        Jedis jedis = jedisPool.getResource();
        String value = UUID.randomUUID().toString();
        try {
            String ret = jedis.set(LOCK_NODE, value, "NX", "PX", timeUnit.toSeconds(10000));
            if (!StringUtils.isEmpty(ret) && "OK".equals(ret)) {
                threadLocal.set(value);
            }
        } finally {
            jedis.close();
        }
    }

    @Override
    public void lock(long waitTime, long leaseTime, TimeUnit timeUnit) {
        // TODO
    }

    @Override
    public void unLock() {
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    @Override
    public boolean isLock() {
        // TODO
        return false;
    }

    @Override
    public boolean isHeldByCurrentThread() {
        // TODO
        return false;
    }
}
