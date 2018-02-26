package com.yudy.centaur.monitor;

import com.yudy.centaur.server.ServerConfig;

import java.io.Closeable;
import java.io.IOException;

public class MonitorHandler extends Thread implements Closeable{

    private final ServerConfig config;

    private final int monitorPort;


    public MonitorHandler(ServerConfig config){
        this.config=config;
        this.monitorPort=config.getMonitorPort();
    }








    @Override
    public void run() {
        //TODO
        super.run();
    }

    @Override
    public void close() throws IOException {
        //TODO

    }
}
