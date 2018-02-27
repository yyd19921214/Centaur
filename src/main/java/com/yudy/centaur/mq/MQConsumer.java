package com.yudy.centaur.mq;

import com.yudy.centaur.db.PersistThread;
import com.yudy.centaur.db.SaveAdaptor;
import com.yudy.centaur.log.LogConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class MQConsumer<T> implements Runnable {

    private final BlockingQueue<T> mq;

    private final long sleepTime;

    private final int mqBatchSize;

    private final MQConfig config;

    private boolean flag = false;

    private long lastDrainTime = System.currentTimeMillis();

    private final List<T> tempList;

    private final LogConfig logConfig;

    public MQConsumer(BlockingQueue<T> mq, MQConfig mqConfig) {
        this.mq = mq;
        this.config = mqConfig;
        sleepTime = config.getSleepTime();
        mqBatchSize = config.getBatchSize();
        tempList = new ArrayList<>(mqBatchSize);
        logConfig = new LogConfig(config.getProp());
    }

    @Override
    public void run() {
        for (; ; ) {
            if (mq.size() >= mqBatchSize) {
                mq.drainTo(tempList, mqBatchSize);
                lastDrainTime = System.currentTimeMillis();
                flag = true;
                boolean result = SaveAdaptor.save2MongoDB(tempList);
                if (!result) {
                    new PersistThread<>(mq,tempList,logConfig).start();
                }
            } else {
                //TODO
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            boolean timeCondition = (System.currentTimeMillis() - lastDrainTime > 3000);
            if (timeCondition && flag) {
                mq.drainTo(tempList,mqBatchSize);
                boolean result=SaveAdaptor.save2MongoDB(tempList);
                if(!result){
                    new PersistThread<>(mq,tempList,logConfig).start();
                }

            }

        }


    }
}
