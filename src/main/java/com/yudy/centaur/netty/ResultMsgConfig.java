package com.yudy.centaur.netty;

import com.yudy.centaur.util.Config;
import com.yudy.centaur.util.PropertiesUtil;

import java.util.Properties;

public class ResultMsgConfig extends Config {

    public ResultMsgConfig(Properties prop) {
        super(prop);
    }

    public int getSuccCode(){
        return PropertiesUtil.getInt(prop,"resultMsg.succCode",200);
    }

    public String getSuccMsg(){
        return PropertiesUtil.getString(prop,"resultMsg.succMsg","successful");
    }
}
