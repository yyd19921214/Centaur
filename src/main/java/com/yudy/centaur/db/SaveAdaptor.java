package com.yudy.centaur.db;

import com.mongodb.DBObject;
import com.yudy.centaur.db.mongo.MongoSaveUtil;
import com.yudy.centaur.util.ObjConvertUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SaveAdaptor<T> {

    private static Logger LOG=Logger.getLogger(SaveAdaptor.class);



    public static <T> boolean save2MongoDB(T t){
        if (t==null)
            return false;
        DBObject obj= ObjConvertUtil.convert2DBObj(t);
        String tableName=DeliverMessage.getTableNameByTime();
        boolean result= MongoSaveUtil.insert(obj,tableName);
        return result;
    }

    public static <T> boolean save2MongoDB(List<T> tempList){
        if(tempList==null||tempList.size()==0)
            return false;
        List<DBObject> list=new ArrayList<>(tempList.size());
        String tableName=DeliverMessage.getTableNameByTime();
        for(T t : tempList){
            DBObject obj=ObjConvertUtil.convert2DBObj(t);
            list.add(obj);
        }

        boolean result=MongoSaveUtil.insertList(list,tableName);
        return result;
    }
}
