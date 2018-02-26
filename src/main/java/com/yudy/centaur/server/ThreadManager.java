package com.yudy.centaur.server;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class ThreadManager<T> implements Closeable {

//    private final ServerConfig config;

//    private final File logDir;

    private BlockingQueue<T> mq;







    @Override
    public void close() throws IOException {

    }
}
