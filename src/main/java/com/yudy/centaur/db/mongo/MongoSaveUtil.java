package com.yudy.centaur.db.mongo;


import com.mongodb.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;

public class MongoSaveUtil {
    private static Logger LOG = Logger.getLogger(MongoSaveUtil.class);

    public static boolean insertList(List<DBObject> dblist, String tableName) {
        boolean result = false;
        if (StringUtils.isEmpty(tableName) || StringUtils.isBlank(tableName) || dblist == null || dblist.size() == 0)
            return result;
        DB db;
        DBCollection collection;
//        WriteResult wr = null;

        try {
            db = MongoUtil.getDB();
            collection = db.getCollection(tableName);
            collection.insert(dblist, WriteConcern.SAFE);
            //TODO
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            return result;
        }
    }

    public static boolean insert(DBObject obj, String tableName) {
        boolean result = true;
        try {
            DB db = MongoUtil.getDB();
            DBCollection collection = db.getCollection(tableName);
            collection.insert(obj, WriteConcern.SAFE);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            return result;
        }
    }

}
