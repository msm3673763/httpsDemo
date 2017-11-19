package com.masm.utils;

import com.masm.cache.lock.DistributedLock;
import com.masm.cache.lock.redission.DistributedLockFactory;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by masiming on 2017/10/23.
 */
@Configuration
public class RedisUtil {

    private static String redisHost;

    @Value("${spring.redis.host}")
    public void setRedisHost(String host) {
        this.redisHost = host;
    }

    private static String redisPort;

    @Value("${spring.redis.port}")
    public void setRedisPort(String port) {
        this.redisPort = port;
    }

    private static String redisPassword;

    @Value("${spring.redis.password}")
    public void setRedisPassword(String password) {
        this.redisPassword = password;
    }

    /**
     * 锁等待时间
     */
    private static final int LOCK_WAIT_TIME = 10;

    /**
     * 锁超时时间
     */
    private static final int LOCK_LEASE_TIME = 60 * 5;

    private static final ConcurrentHashMap<String, DistributedLockFactory> lockMap = new ConcurrentHashMap<String, DistributedLockFactory>();

    /**
     * 分布式锁设置
     *
     * @param key
     * @return
     */
    public static DistributedLock setLock(String key, Integer... times) {
        String singleURL = "redis://".concat(redisHost).concat(":").concat(redisPort);
        DistributedLockFactory lockFactory = lockMap.get(singleURL);

        lockFactory = checkExist(singleURL, lockFactory);

        DistributedLock lock = lockFactory.getLock(key);

        Integer waitTime = (times.length > 0 && times[0] > 0) ? times[0] : LOCK_WAIT_TIME;
        Integer leaseTime = (times.length > 1 && times[1] > 0) ? times[1] : LOCK_LEASE_TIME;
        lock.lock(waitTime, leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    public static void set(String key, Object value, long timeout) {
        String singleURL = "redis://".concat(redisHost).concat(":").concat(redisPort);
        DistributedLockFactory lockFactory = lockMap.get(singleURL);

        lockFactory = checkExist(singleURL, lockFactory);

        RedissonClient client = lockFactory.getRedissonClient();
        client.getBucket(key).set(value, timeout, TimeUnit.SECONDS);
    }

    public static Object get(String key) {
        String singleURL = "redis://".concat(redisHost).concat(":").concat(redisPort);
        DistributedLockFactory lockFactory = lockMap.get(singleURL);

        lockFactory = checkExist(singleURL, lockFactory);

        RedissonClient client = lockFactory.getRedissonClient();
        return client.getBucket(key).get();
    }

    private static DistributedLockFactory checkExist(String singleURL, DistributedLockFactory lockFactory) {
        if (Objects.isNull(lockFactory)) {
            lockFactory = new DistributedLockFactory(singleURL);
            if (!StringUtils.isEmpty(redisPassword)) {
                lockFactory = new DistributedLockFactory(singleURL, 30000, redisPassword);
            }
            lockMap.put(singleURL, lockFactory);
        }
        return lockFactory;
    }

}
