package com.yudy.centaur.server;

import com.yudy.centaur.log.LogConfig;
import com.yudy.centaur.monitor.MonitorHandler;
import com.yudy.centaur.mq.MQConfig;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class ThreadManager<T> implements Closeable {

    private final ServerConfig config;

    private final File logDir;

    private BlockingQueue<T> mq;

    private MonitorHandler monitorHandler;

    private ExecutorService servicePool;

    private final int persistBatchSize;

    private final List<T> tempList;

    private final LogConfig logConfig;

    private final MQConfig mqConfig;

    private final int mqConsumerNum;

    private final boolean needRecovery;


    public ThreadManager(ServerConfig config,boolean needRecovery) throws IOException {
        this.config=config;
        this.logDir=new File(config.getLogDir()).getCanonicalFile();

        mq=new LinkedBlockingDeque<T>(config.getMQInitSize());

        this.persistBatchSize=config.getPersistBatchSize();
        this.tempList=new ArrayList<T>(persistBatchSize);
        this.logConfig=new LogConfig(config.getProp());
        this.mqConfig=new MQConfig(config.getProp());
        this.mqConsumerNum=mqConfig.getThreadNum();
        servicePool= Executors.newCachedThreadPool();
        this.needRecovery=needRecovery;
        if (needRecovery){
            //TODO
        }

    }

    public void start(){
        for(int i=0;i<mqConsumerNum;i++){
//            servicePool.execute(new M);
        }
    }








    @Override
    public void close() throws IOException {

    }
}
