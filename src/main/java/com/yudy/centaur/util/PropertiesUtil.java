package com.yudy.centaur.util;

import java.io.File;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties loadConfigProperties(File centaurConfigFile) {
        //TODO
        return new Properties();
    }

    public static Properties loadLog4jProperties(File centaurLog4jFile) {
        //TODO
        return new Properties();
    }

    public static int getInt(Properties prop, String param, int defaultVal) {
        String defaultStr = String.valueOf(defaultVal);
        return Integer.parseInt(prop.getProperty(param, defaultStr));

    }

    public static int getIntInRange(Properties prop, String param, int defaultVal, int min, int max) {
        String val = prop.getProperty(param);
        if (val == null)
            return defaultVal;
        long valInt = Long.parseLong(val);
        if (valInt < min || valInt > max)
            return defaultVal;
        else
            return (int) valInt;


    }

    public static boolean getBoolean(Properties prop, String param, boolean defaultVal) {
        String defaultBool = String.valueOf(defaultVal);
        return Boolean.valueOf(prop.getProperty(param, defaultBool));
    }

    public static String getString(Properties prop, String param) {
        return prop.getProperty(param);
    }

    public static String getString(Properties prop, String param, String defaultVal) {
        return prop.getProperty(param, defaultVal);
    }


}
