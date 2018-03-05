package com.yudy.centaur.zk;

import com.yudy.centaur.util.Constance;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ServiceConsumer {

    private static final Logger LOGGER = Logger.getLogger(ServiceConsumer.class);

    private CountDownLatch latch=new CountDownLatch(1);

    private volatile List<String> urlList=new ArrayList<>();

    public ServiceConsumer(){

    }

    private ZooKeeper connectServer(){
        ZooKeeper zk=null;
        try {
            zk=new ZooKeeper(Constance.ZK_CONNECTION_STRING, Constance.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getState()==Event.KeeperState.SyncConnected)
                        latch.countDown();
                }
            });
            latch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zk;

    }

    private void watchNode(final ZooKeeper zk) throws KeeperException, InterruptedException {
        List<String> nodelist=zk.getChildren(Constance.ZK_REGISTRY_PATH, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getType()==Event.EventType.NodeChildrenChanged)
                    try {
                        watchNode(zk);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        });
        List<String> dataList=new ArrayList<>();
        
    }




}
