package com.masm.service.impl;

import com.masm.cache.lock.DistributedLock;
import com.masm.service.CacheLoadable;
import com.masm.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by masiming on 2017/11/25 17:11.
 */
@Component
@Slf4j
public class CacheTemplate<T> {

    private static final String CACHE_BACKUP = "_backup";

    public T getCache(String key, long expire, TimeUnit unit, CacheLoadable<T> cacheLoadable) {
        //主备缓存解决缓存雪崩
        Object value = RedisUtil.get(key);//从redis取数据
        if (Objects.nonNull(value)) {
            return (T) value;
        }

        DistributedLock lock = null;
        try {
            lock = RedisUtil.setLock("LOCK_" + key);//尝试获取分布式锁，获取不到抛出异常

            //获取到锁，查询数据库，并将值重新设置到主缓存和备份缓存
            value = cacheLoadable.load();
            if (Objects.nonNull(value)) {
                RedisUtil.set(key, value, expire, unit);
                RedisUtil.set(key.concat(CACHE_BACKUP), value, expire + 1000, unit);
            }
            return (T) value;
        } catch(Exception e) {//获取分布式锁失败，从备份缓存中取
            log.error(e.getMessage(), e);
            return (T) RedisUtil.get(key.concat(CACHE_BACKUP));
        } finally {
            if (lock != null) {//释放锁
                lock.unLock();
            }
        }
    }
}
