package com.yudy.centaur.log;

import com.yudy.centaur.util.Config;
import com.yudy.centaur.util.PropertiesUtil;

import java.util.Properties;

public class LogConfig extends Config{
    public LogConfig(Properties prop){
        super(prop);
    }

    public String getDir(){
        return PropertiesUtil.getString(prop,"log.dir");
    }

    public String getPrefix(){
        return PropertiesUtil.getString(prop,"log.prefix","log_");
    }

    public int getMaxSize(){
        return PropertiesUtil.getInt(prop,"log.maxsize",16);
    }

}
