package com.masm.cache.lock.redission;

import com.masm.cache.lock.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * Created by masiming on 2017/10/23.
 * 工具类：获取分布式锁对象
 */
@Slf4j
public class DistributedLockFactory {

    /**
     * Redis key 值默认前缀
     */
    public static final String LOCK_KEY_PREFIX = "REDISSON_LOCK_KEY_";

    private static final int DEFAULT_TIME_OUT = 30000;

    private RedissonClient client;

    /**
     * 创建redission客户端连接
     * @param singleURL
     *      redis连接地址
     */
    public DistributedLockFactory(String singleURL) {
        this(singleURL, DEFAULT_TIME_OUT);
    }

    /**
     * 创建redission客户端连接
     * @param singleURL
     *          redis连接地址
     * @param timeout
     *      连接响应超时时间
     */
    public DistributedLockFactory(String singleURL, int timeout) {
        this(singleURL, timeout, null);
    }

    /**
     * 创建redission客户端连接
     * @param singleURL
     *      redis连接地址
     * @param timeout
     *      连接响应超时时间
     * @param password
     *      redis密码
     */
    public DistributedLockFactory(String singleURL, int timeout, String password) {
        this(singleURL, timeout, password, 0);
    }

    /**
     *  创建redission客户端连接
     * @param singleURL
     *      redis连接地址
     * @param timeout
     *      连接响应超时时间
     * @param password
     *      redis密码
     * @param database
     *      redis数据库
     */
    public DistributedLockFactory(String singleURL, int timeout, String password, Integer database) {
        Config config = new Config();
        config.useSingleServer().setAddress(singleURL)
                .setPassword(password)
                .setDatabase(database)
                .setTimeout(timeout);
        this.client = Redisson.create(config);
    }

    /**
     *  创建redission客户端连接
     * @param config
     *      redission提供的配置对象
     */
    public DistributedLockFactory(Config config) {
        this.client = Redisson.create(config);
    }

    /**
     * 根据Key值，获取分布式锁
     * @param key
     * @return
     */
    public DistributedLock getLock(String key) {
        RLock rLock = client.getLock(LOCK_KEY_PREFIX + key);
        return new RedissionDistributedLock(rLock);
    }
}
