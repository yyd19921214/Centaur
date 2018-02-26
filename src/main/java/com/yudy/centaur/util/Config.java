package com.yudy.centaur.util;

import java.util.Properties;

abstract public class Config {
    protected Properties prop;

    public Config(Properties prop){
        this.prop=prop;
    }

    public Properties getProp() {
        return prop;
    }
}
