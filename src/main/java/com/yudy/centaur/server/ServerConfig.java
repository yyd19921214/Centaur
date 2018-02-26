package com.yudy.centaur.server;

import com.yudy.centaur.util.Config;
import com.yudy.centaur.util.PropertiesUtil;

import java.util.Properties;

public class ServerConfig extends Config {
    Properties prop;

    public ServerConfig(Properties prop) {
        super(prop);
    }

    public int getPort() {
        return PropertiesUtil.getInt(prop, "port", 8088);
    }

    public int getMaxConnections() {
        return PropertiesUtil.getInt(prop, "max.connections", 10000);
    }


    public int getMaxMessageSize() {
        return PropertiesUtil.getIntInRange(prop, "max.message.size", 1024 * 1024, 0, Integer.MAX_VALUE);
    }


    public int getDefaultFlushIntervalMs() {
        return PropertiesUtil.getInt(prop, "log.default.flush.interval.ms", getFlushSchedulerThreadRate());
    }


    public int getFlushSchedulerThreadRate() {
        return PropertiesUtil.getInt(prop, "log.default.flush.scheduler.interval.ms", 3000);

    }

    public boolean getEnableZookeeper() {
        return PropertiesUtil.getBoolean(prop, "enable.zookeeper", false);
    }

    public String getLogDir() {
        return PropertiesUtil.getString(prop, "log.dir");
    }

    public int getMQInitSize() {
        return PropertiesUtil.getInt(prop, "mq.init.size", 50000);
    }

    public int getEnqueueTimeoutMs() {
        return PropertiesUtil.getInt(prop, "enqueue.timeout", -1);
    }


    public int getMonitorPort() {
        return PropertiesUtil.getInt(prop, "monitor.port", 8888);
    }

    public String getUsername() {
        return PropertiesUtil.getString(prop, "db.username", "root");
    }

    public String getPassword() {
        return PropertiesUtil.getString(prop, "db.password", "root");
    }

    // protobuf class
    public String getPBLogClassName() {
        return PropertiesUtil.getString(prop, "pb.log.class");
    }

    public String getLogTransferType() {
        return PropertiesUtil.getString(prop, "log.transfer.type", "json");
    }

    public int getPersistBatchSize() {
        return PropertiesUtil.getInt(prop, "persist.batch.size", 10000);
    }

    public String getProtoFileDir() {
        return PropertiesUtil.getString(prop, "proto.file.dir", "proto");
    }


}
