package com.yudy.centaur.netty;

import com.yudy.centaur.server.ServerConfig;
import com.yudy.centaur.server.ThreadManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ProtobufNettyServerHandler<T> extends SimpleChannelInboundHandler<T> {

    private final ThreadManager<T> threadManager;

    private final ServerConfig config;

    private final long enqueueTimeoutMs;

    private final int persistBatchSize;

    private final List<T> tempList;

    public ProtobufNettyServerHandler(ThreadManager<T> threadManager,ServerConfig serverConfig){
        this.threadManager=threadManager;
        this.config=serverConfig;

        enqueueTimeoutMs=config.getEnqueueTimeoutMs();
        persistBatchSize=config.getPersistBatchSize();
        tempList=new ArrayList<>(persistBatchSize);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        T data = (T) msg;
        BlockingQueue<T> queue = threadManager.getMq();
        boolean added = false;
        if (data != null) {
            if (enqueueTimeoutMs == 0)
                added = queue.offer(data);//直接插入队列，若队列满则返回false;
            else if (enqueueTimeoutMs < 0) {
                queue.put(data);//如果队列满则只能一直等待
                added = true;
            } else
                added = queue.offer(data, enqueueTimeoutMs, TimeUnit.MILLISECONDS);//等待一段时间后判断是否能够插入

        }
        ctx.channel().writeAndFlush("ok".getBytes());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, T t) throws Exception {
        System.out.println("messageReceived" + t);
    }
}
