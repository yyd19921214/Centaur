package com.yudy.centaur.mq;

import com.yudy.centaur.util.Config;
import com.yudy.centaur.util.PropertiesUtil;

import java.util.Properties;

public class MQConfig extends Config{
    public MQConfig(Properties prop){
        super(prop);
    }

    public long getSleepTime(){
        return PropertiesUtil.getLong(prop,"mq.sleep.time",50L);
    }

    //mq初始化数组大小
    public int getInitSize(){
        return PropertiesUtil.getInt(prop,"mq.init.size",5000);
    }

    public int getMaxSize(){
        return PropertiesUtil.getInt(prop,"mq.max.size",1000000);
    }

    public int getBatchSize(){
        return PropertiesUtil.getInt(prop,"mq.batch.size",1000);
    }

    public int getSafeNum(){
        return PropertiesUtil.getInt(prop,"mq.safe.num",25000);
    }

    public int getWarnNum(){
        return PropertiesUtil.getInt(prop,"mq.warn.num",75000);
    }

    public int getLogMaxSize(){
        return PropertiesUtil.getInt(prop,"log.size",16);
    }

    public int getThreadNum(){
        return PropertiesUtil.getInt(prop,"mq.thread.num",10);
    }

    public String getLogDir(){
        return PropertiesUtil.getString(prop,"log.dir","/logs");
    }

}
