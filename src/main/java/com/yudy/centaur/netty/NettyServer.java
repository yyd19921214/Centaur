package com.yudy.centaur.netty;

import com.yudy.centaur.server.ServerConfig;
import com.yudy.centaur.server.ThreadManager;
import com.yudy.centaur.util.Constance;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;

public class NettyServer<T> extends Thread implements Closeable{
    private static Logger LOG= Logger.getLogger(NettyServer.class);

    private ThreadManager<T> threadManager;
    private ServerConfig config;
    private int port;
    private String logTransferType;

    public NettyServer(int port){
        this.port=port;
    }

    public NettyServer(ThreadManager<T> threadManager,ServerConfig config){

        this.threadManager=threadManager;
        this.config=config;
        this.port=config.getPort();
        this.logTransferType=config.getLogTransferType();
    }




    @Override
    public void run() {
        try{
            ServerBootstrap b=new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG,1024);
            ChannelHandler childHandler=null;
            if(logTransferType.equalsIgnoreCase(Constance.LOG_TYPE_PB))
            {
                //TODO
            }
            else{
                //TODO
            }



        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

    }

    @Override
    public void close() throws IOException {

    }
}
