package com.yudy.centaur.server;

import com.yudy.centaur.db.PersistThread;
import com.yudy.centaur.log.LogConfig;
import com.yudy.centaur.monitor.MonitorHandler;
import com.yudy.centaur.mq.MQConfig;
import com.yudy.centaur.mq.MQConsumer;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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

    // 这个锁是在创建日志文件，删除日志文件时候使用,同步加锁，暂未实现
    private final Object logCreationLock = new Object();


    public ThreadManager(ServerConfig config, boolean needRecovery) throws IOException {
        this.config = config;
        this.logDir = new File(config.getLogDir()).getCanonicalFile();

        mq = new LinkedBlockingDeque<T>(config.getMQInitSize());

        this.persistBatchSize = config.getPersistBatchSize();
        this.tempList = new ArrayList<T>(persistBatchSize);
        this.logConfig = new LogConfig(config.getProp());
        this.mqConfig = new MQConfig(config.getProp());
        this.mqConsumerNum = mqConfig.getThreadNum();
        servicePool = Executors.newCachedThreadPool();
        this.needRecovery = needRecovery;
        if (needRecovery) {
            //TODO
        }

    }

    public void start() {
        for (int i = 0; i < mqConsumerNum; i++) {
            servicePool.execute(new MQConsumer<>(mq, mqConfig));
        }
    }

    public void flushAllLogs() {
        //TODO
        //flush 时直接写磁盘？
        while (mq.peek() != null) {
            mq.drainTo(tempList, persistBatchSize);
            servicePool.execute(new PersistThread<>(mq, tempList, logConfig));
            tempList.clear();

        }
    }


    @Override
    public void close() {
        flushAllLogs();
        servicePool.shutdown();
        try {
            servicePool.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public BlockingQueue<T> getMq() {
        return mq;
    }

    public void setMq(BlockingQueue<T> mq) {
        this.mq = mq;
    }

    public MonitorHandler getMonitorHandler() {
        return monitorHandler;
    }

    public void setMonitorHandler(MonitorHandler monitorHandler) {
        this.monitorHandler = monitorHandler;
    }

    public ExecutorService getServicePool() {
        return servicePool;
    }

    public void setServicePool(ExecutorService servicePool) {
        this.servicePool = servicePool;
    }

    public ServerConfig getConfig() {
        return config;
    }

    public File getLogDir() {
        return logDir;
    }

    public boolean isNeedRecovery() {
        return needRecovery;
    }

    public Object getLogCreationLock() {
        return logCreationLock;
    }

    public int getPersistBatchSize() {
        return persistBatchSize;
    }


    public List<T> getTempList() {
        return tempList;
    }

    public LogConfig getLogConfig() {
        return logConfig;
    }

    public MQConfig getMqConfig() {
        return mqConfig;
    }

    public int getMqConsumeNum() {
        return mqConsumerNum;
    }
}
