package com.yudy.centaur.db;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

public class DeliverMessage {

    public static String getTableNameByTime() {
        String month = DateFormatUtils.format(new Date(), "yyyy-MM");
        return month;
    }


}
