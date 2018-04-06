package com.masm.cache.lock.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author masiming
 * @create 2017/12/09
 **/
public class CuratorTest {

    private static final String CONNECT_ADDR = "192.168.31.128:2181,192.168.31.129:2181,192.168.31.130:2181";
    //session超时时间 单位ms
    private static final int SESSION_TIMEOUT = 10000;

    public static void main(String[] args) throws Exception {
        //1、重试策略：初试时间为1s，重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);

        //2、通过工厂创建连接
        CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR)
                //连接超时时间，默认为15000ms
                .connectionTimeoutMs(10000)
                //会话超时时间，默认为60000毫秒
                .sessionTimeoutMs(SESSION_TIMEOUT)
                //有四种重试策略：ExponentialBackoffRetry、RetryNTimes、RetryOneTimes、RetryUntilElapsed
                .retryPolicy(retryPolicy)
                .build();

        //3、开启连接
        cf.start();

        //创建一个cache缓存
        final NodeCache cache = new NodeCache(cf, "/testRoot", false);
        cache.start(true);
        cache.getListenable().addListener(new NodeCacheListener() {
            /**
             * 触发事件为创建节点和更新节点，在删除节点的时候并不触发此操作
             * @throws Exception
             */
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("路径为：" + cache.getCurrentData().getPath());
                System.out.println("数据为：" + new String(cache.getCurrentData().getData()));
                System.out.println("状态为：" + cache.getCurrentData().getStat());
                System.out.println("-------------------------------------------");
            }
        });

        PathChildrenCache childrenCache = new PathChildrenCache(cf, "/testRoot", true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            /**
             * 监听子节点的新增、修改、删除
             * @param curatorFramework
             * @param pathChildrenCacheEvent
             * @throws Exception
             */
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                switch (pathChildrenCacheEvent.getType()) {
                    case CHILD_ADDED:
                        System.out.println("CHILD_ADDED：" + pathChildrenCacheEvent.getData().getPath());
                        System.out.println("CHILD_ADDED：" + new String(pathChildrenCacheEvent.getData().getData()));
                        System.out.println("CHILD_ADDED：" + pathChildrenCacheEvent.getData().getStat());
                        break;
                    case CHILD_UPDATED:
                        System.out.println("CHILD_UPDATED：" + pathChildrenCacheEvent.getData().getPath());
                        System.out.println("CHILD_UPDATED：" + new String(pathChildrenCacheEvent.getData().getData()));
                        System.out.println("CHILD_UPDATED：" + pathChildrenCacheEvent.getData().getStat());
                        break;
                    case CHILD_REMOVED:
                        System.out.println("CHILD_REMOVED：" + pathChildrenCacheEvent.getData().getPath());
                        System.out.println("CHILD_REMOVED：" + new String(pathChildrenCacheEvent.getData().getData()));
                        System.out.println("CHILD_REMOVED：" + pathChildrenCacheEvent.getData().getStat());
                        break;
                    default:
                        break;
                }
            }
        });

        //4、建立节点 指定节点类型（不加withMode默认为持久类型节点）、路径、数据内容
        cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/testRoot/ca1", "ca1内容".getBytes());

        //5、读取节点
        String value = new String(cf.getData().forPath("/testRoot/ca1"));
        System.out.println(value);

        //6、修改节点
        cf.setData().forPath("/testRoot/ca1", "dfs".getBytes());
        value = new String(cf.getData().forPath("/testRoot/ca1"));
        System.out.println(value);

        //7、绑定回调函数
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 10,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(3), new ThreadPoolExecutor.DiscardPolicy());
        cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("code：" + curatorEvent.getResultCode());
                        System.out.println("type：" + curatorEvent.getType());
                        System.out.println("线程为：" + Thread.currentThread().getName());
                    }
                }, executor).forPath("/testRoot/ca2", "ca2内容".getBytes());
        System.out.println("主线程：" + Thread.currentThread().getName());
        Thread.sleep(30000);

        //8、读取子节点getChildren方法
        List<String> list = cf.getChildren().forPath("/testRoot");
        for (String child : list) {
            System.out.println(child);
        }

        //9、判断节点是否存在：stat为null则不存在，否则存在
        Stat stat = cf.checkExists().forPath("/testRoot/ca3");
        System.out.println(stat);

        //0、删除节点
        cf.delete().guaranteed().deletingChildrenIfNeeded().forPath("/testRoot");

        //11、关闭连接
        cf.close();
    }
}
