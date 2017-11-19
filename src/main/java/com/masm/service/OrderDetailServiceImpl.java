package com.masm.service;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.masm.bean.TOrderDetail;
import com.masm.cache.lock.DistributedLock;
import com.masm.utils.RedisUtil;
import com.masm.mapper.TOrderDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Created by masiming on 2017/11/18 22:19.
 * 缓存击穿和缓存雪崩解决方案
 */
@Slf4j
@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private static final String CACHE_BACKUP = "_backup";

    @Autowired
    private TOrderDetailMapper orderDetailMapper;

    private BloomFilter<String> bloomFilter;

    @PostConstruct
    private void init() {
        List<String> userIds = orderDetailMapper.getDistinctUserIds();
        if (!CollectionUtils.isEmpty(userIds)) {
            bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), userIds.size());
            for (String userId : userIds) {
                bloomFilter.put(userId);
            }
        }
    }

    @Override
    public int addOrderDetail(TOrderDetail orderDetail) {
        return orderDetailMapper.insert(orderDetail);
    }

    @Override
    public int delOrderDetail(String id) {
        return orderDetailMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateOrderDetail(TOrderDetail orderDetail) {
        return orderDetailMapper.updateByPrimaryKeySelective(orderDetail);
    }

    @Override
    public TOrderDetail getOrderDetail(String id) {
        return orderDetailMapper.selectByPrimaryKey(id);
    }

    /**
     * 缓存击穿和缓存雪崩解决方案
     * @param userId
     * @return
     */
    @Override
    public BigDecimal getOrderAmount(String userId) {
        if (bloomFilter!=null && !bloomFilter.mightContain(userId)) {//解决缓存击穿
            return null;
        }

        //主备缓存解决缓存雪崩
        BigDecimal cacheResult = (BigDecimal) RedisUtil.get(userId);//从redis取数据
        if (!Objects.isNull(cacheResult)) {
            return cacheResult;
        }

        DistributedLock lock = null;
        try {
            lock = RedisUtil.setLock("LOCK_" + userId);//尝试获取分布式锁，获取不到抛出异常

            //获取到锁，查询数据库，并将值重新设置到主缓存和备份缓存
            BigDecimal orderAmount = orderDetailMapper.getOrderAmount(userId);
            if (!Objects.isNull(orderAmount)) {
                RedisUtil.set(userId, orderAmount, 1000);
                RedisUtil.set(userId.concat(CACHE_BACKUP), orderAmount, 1000);
            }
            return orderAmount;
        } catch(Exception e) {//获取分布式锁失败，从备份缓存中取
            log.error(e.getMessage(), e);
            return (BigDecimal) RedisUtil.get(userId.concat(CACHE_BACKUP));
        } finally {
            if (lock != null) {//释放锁
                lock.unLock();
            }
        }
    }
}
