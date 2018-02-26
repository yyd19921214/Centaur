package com.yudy.centaur.server;


import com.yudy.centaur.util.Constance;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 服务器类，负责启动各种Netty监听服务，各种系统线程
 * @param <T>
 */
public class Server<T> {
    private final ServerConfig config;

    private final Logger LOG= Logger.getLogger(Server.class);

    private final File logDir;

    private int port=-1;

    private final CountDownLatch shutdownLatch=new CountDownLatch(1);

    private final AtomicBoolean isShuttingDown=new AtomicBoolean(false);

    private final  String CLENN_SHUTDOWN_FILE=Constance.CLEAN_SHUTDOWN_FILE;

    public Server(ServerConfig config) {
        this.config = config;
        logDir=new File(config.getLogDir());
        if(!logDir.exists()){
            LOG.warn("没有创建存放日志持久化的文件夹!!!系统正在创建");
            logDir.mkdir();
        }
        port=config.getPort();
    }

    public void startup(){
        final long startms=System.currentTimeMillis();
        LOG.info("启动日志服务器"+port);
        // 本地是否存在.cleanshutdown文件，如果存在，表明上一次是正常关闭
        boolean needRecovery=true;
        File cleanShutDownFile=new File(new File(config.getLogDir()),CLENN_SHUTDOWN_FILE);
        if(cleanShutDownFile.exists()){
            needRecovery=false;
            cleanShutDownFile.delete();
        }



    }

    public void close(){;}

    public void awaitShutDown() throws InterruptedException{
        ;
    }



}
