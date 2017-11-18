package com.masm.utils;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by masiming on 2017/11/19 0:40.
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

    private volatile static RedisUtil redisUtil;

    private static RedissonClient redisson;

    private RedisUtil(){}

    static {
        Config config = new Config();
        config.useSingleServer().setAddress(redisHost)
                .setPassword(redisPassword)
                .setDatabase(0)
                .setTimeout(10000);
        redisson = Redisson.create(config);
    }

    /**
     * 提供单例模式
     * @return
     */
    public static RedisUtil getInstance() {
        if (redisUtil == null) {
            synchronized (RedisUtil.class) {
                if (redisUtil == null) {
                    redisUtil = new RedisUtil();
                }
            }
        }
        return redisUtil;
    }

    /**
     * 使用config创建Redisson
     * Redisson是用于连接Redis Server的基础类
     * @param config
     * @return
     */
    public RedissonClient getRedisson(Config config) {
        return Redisson.create(config);
    }

    /**
     * 使用ip地址和端口创建Redisson
     * @return
     */
    public RedissonClient getRedisson() {
        Config config = new Config();
        config.useSingleServer().setAddress(redisHost)
                .setPassword(redisPassword)
                .setDatabase(0)
                .setTimeout(10000);
        return Redisson.create(config);
    }

    public static void set(String key, Object value, long timeout) {
        RBucket<Object> bucket = redisson.getBucket(key);
        bucket.set(value, timeout, TimeUnit.MILLISECONDS);
    }

    public static Object get(String key) {
        RBucket<Object> bucket = redisson.getBucket(key);
        return bucket.get();
    }

    /**
     * 关闭Redisson客户端连接
     * @param redisson
     */
    public static void closeRedisson(RedissonClient redisson) {
        redisson.shutdown();
        System.out.println("成功关闭Redis Client连接");
    }

    /**
     * 获取字符串对象
     * @param redisson
     * @param objectName
     * @return
     */
    public <T> RBucket<T> getRBucket(Redisson redisson, String objectName){
        RBucket<T> bucket = redisson.getBucket(objectName);
        return bucket;
    }

    /**
     * 获取Map对象
     * @param redisson
     * @param objectName
     * @return
     */
    public <K,V> RMap<K, V> getRMap(Redisson redisson, String objectName){
        RMap<K, V> map = redisson.getMap(objectName);
        return map;
    }

    /**
     * 获取有序集合
     * @param redisson
     * @param objectName
     * @return
     */
    public <V> RSortedSet<V> getRSortedSet(Redisson redisson, String objectName){
        RSortedSet<V> sortedSet = redisson.getSortedSet(objectName);
        return sortedSet;
    }

    /**
     * 获取集合
     * @param redisson
     * @param objectName
     * @return
     */
    public <V> RSet<V> getRSet(Redisson redisson, String objectName){
        RSet<V> rSet = redisson.getSet(objectName);
        return rSet;
    }

    /**
     * 获取列表
     * @param redisson
     * @param objectName
     * @return
     */
    public <V> RList<V> getRList(Redisson redisson, String objectName){
        RList<V> rList = redisson.getList(objectName);
        return rList;
    }

    /**
     * 获取队列
     * @param redisson
     * @param objectName
     * @return
     */
    public <V> RQueue<V> getRQueue(Redisson redisson,String objectName){
        RQueue<V> rQueue = redisson.getQueue(objectName);
        return rQueue;
    }

    /**
     * 获取双端队列
     * @param redisson
     * @param objectName
     * @return
     */
    public <V> RDeque<V> getRDeque(Redisson redisson,String objectName){
        RDeque<V> rDeque = redisson.getDeque(objectName);
        return rDeque;
    }

    /**
     * 此方法不可用在Redisson 1.2 中
     * 在1.2.2版本中 可用
     * @param redisson
     * @param objectName
     * @return
     */
    /**
     public <V> RBlockingQueue<V> getRBlockingQueue(Redisson redisson,String objectName){
     RBlockingQueue rb=redisson.getBlockingQueue(objectName);
     return rb;
     }*/

    /**
     * 获取锁
     * @param redisson
     * @param objectName
     * @return
     */
    public RLock getRLock(Redisson redisson,String objectName){
        RLock rLock = redisson.getLock(objectName);
        return rLock;
    }

    /**
     * 获取原子数
     * @param redisson
     * @param objectName
     * @return
     */
    public RAtomicLong getRAtomicLong(Redisson redisson,String objectName){
        RAtomicLong rAtomicLong = redisson.getAtomicLong(objectName);
        return rAtomicLong;
    }

    /**
     * 获取记数锁
     * @param redisson
     * @param objectName
     * @return
     */
    public RCountDownLatch getRCountDownLatch(Redisson redisson,String objectName){
        RCountDownLatch rCountDownLatch = redisson.getCountDownLatch(objectName);
        return rCountDownLatch;
    }

    /**
     * 获取消息的Topic
     * @param redisson
     * @param objectName
     * @return
     */
    public <M> RTopic<M> getRTopic(Redisson redisson,String objectName){
        RTopic<M> rTopic = redisson.getTopic(objectName);
        return rTopic;
    }
}
