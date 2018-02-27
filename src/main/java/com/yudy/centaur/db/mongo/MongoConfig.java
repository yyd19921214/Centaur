package com.yudy.centaur.db.mongo;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoOptions;
import com.yudy.centaur.util.Config;
import com.yudy.centaur.util.PropertiesUtil;

import java.util.Properties;

public class MongoConfig extends Config{
    public MongoConfig(Properties prop) {
        super(prop);
    }

    public String getIp(){
        return PropertiesUtil.getString(prop,"mongo.ip","127.0.0.1");
    }

    public int getPort(){
        return PropertiesUtil.getInt(prop,"mongo.port",27017);
    }

    public String getUsername(){
        return PropertiesUtil.getString(prop,"mongo.username","root");
    }

    public String getPassword(){
        return PropertiesUtil.getString(prop,"mongo.password","root");
    }

    public String getDbname(){
        return PropertiesUtil.getString(prop,"mongo.dbname","nysyslogs");
    }

    public MongoClientOptions getMongoOptions(){
        MongoClientOptions options=MongoClientOptions.builder()
                .connectionsPerHost(PropertiesUtil.getInt(prop,"mongo.connectHost",10))
                .maxWaitTime(PropertiesUtil.getInt(prop,"mongo.maxwaitTime",120000))
                .socketTimeout(PropertiesUtil.getInt(prop,"mongo.socketTimeout",1000))
                .connectTimeout(PropertiesUtil.getInt(prop,"mongo.connectTimeout",10000))
                .threadsAllowedToBlockForConnectionMultiplier(PropertiesUtil.getInt(prop,"mongo.threadsAllowedToBlockForConnectionMultiplier",5)).build();
        return options;

    }

}
