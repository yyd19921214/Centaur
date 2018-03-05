package com.yudy.centaur.zk;

import com.yudy.centaur.util.Constance;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ServiceProvider {

    private static Logger LOG = Logger.getLogger(ServiceProvider.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public void publish(String host, int port) {
        String url = String.format("%s:%d", host, port);
        ZooKeeper zk = connectServer();
        if (zk != null) {
            createNode(zk, url);

        }

    }


    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(Constance.ZK_CONNECTION_STRING, Constance.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected)
                        latch.countDown();
                }
            });
            latch.await();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zk;
    }

    private void createNode(ZooKeeper zk, String url) {
        try {
            String path = zk.create(Constance.ZK_PROVIDER_PATH, url.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            LOG.debug("create zookeeper node ({} => {})" + path + url);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    private String publishService(String host,String port){
//        String url=String.format("%s:%d",host,port);
//        return url;
//    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8000;
        String url = String.format("%s:%d", host, port);
        System.out.println(url);
    }


}
