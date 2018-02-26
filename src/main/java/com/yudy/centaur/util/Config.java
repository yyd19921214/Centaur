package com.yudy.centaur.util;

import java.util.Properties;

abstract public class Config {
    Properties prop;

    public Config(Properties prop){
        this.prop=prop;
    }
}
