package com.yudy.centaur.db.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoUtil {

    private static MongoClient mongoClient;
    private static MongoConfig config;

    public static DB getDB(){
        return mongoClient.getDB(config.getDbname());
    }

    public static void init(MongoConfig _config){
        config=_config;
        mongoClient=new MongoClient(config.getIp(),config.getPort());
    }

}
