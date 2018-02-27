package com.yudy.centaur.netty;

import com.yudy.centaur.server.ServerConfig;
import com.yudy.centaur.server.ThreadManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class NettyServerHandler<T> extends SimpleChannelInboundHandler<T> {

    private final ThreadManager<T> threadManager;
    private final ServerConfig config;
    private final long enqueueTimeoutMs;

    private HttpRequest request;

    private ResultMsgConfig resultMsgConfig;

    private final int persistBatchSize;

    private final List<T> tempList;

    private final int maxMessageSize;

    public NettyServerHandler(ThreadManager threadManager,ServerConfig serverConfig){
        this.threadManager=threadManager;
        this.config=serverConfig;
        this.enqueueTimeoutMs=config.getEnqueueTimeoutMs();
        this.resultMsgConfig=new ResultMsgConfig(config.getProp());
        this.persistBatchSize=config.getPersistBatchSize();
        this.tempList=new ArrayList<>(persistBatchSize);
        this.maxMessageSize=config.getMaxMessageSize();
    }


    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, T t) throws Exception {

    }
}
