package com.yudy.centaur.netty;

import com.alibaba.fastjson.JSON;
import com.yudy.centaur.db.PersistThread;
import com.yudy.centaur.log.LogConfig;
import com.yudy.centaur.server.ServerConfig;
import com.yudy.centaur.server.ThreadManager;
import com.yudy.centaur.util.Serialization;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class NettyServerHandler<T> extends SimpleChannelInboundHandler<T> {

    private final ThreadManager<T> threadManager;
    private final ServerConfig config;
    private final long enqueueTimeoutMs;

    private ResultMsgConfig resultMsgConfig;

    private final int persistBatchSize;

    private final List<T> tempList;

    private final int maxMessageSize;

    public NettyServerHandler(ThreadManager threadManager, ServerConfig serverConfig) {
        this.threadManager = threadManager;
        this.config = serverConfig;
        this.enqueueTimeoutMs = config.getEnqueueTimeoutMs();
        this.resultMsgConfig = new ResultMsgConfig(config.getProp());
        this.persistBatchSize = config.getPersistBatchSize();
        this.tempList = new ArrayList<>(persistBatchSize);
        this.maxMessageSize = config.getMaxMessageSize();
    }


    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, T t) throws Exception {
        System.out.println("messageReceived " + t);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] bys = Serialization.serialize(msg);
        if (bys.length > maxMessageSize)
            throw new IllegalArgumentException("客户端发送的消息过大!!!长度是:" + bys.length);
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
        ResultMsg resultMsg = new ResultMsg();
        if (added) {
            resultMsg.setErrCode(resultMsgConfig.getSuccCode());
            resultMsg.setErrMsg(resultMsgConfig.getSuccMsg());
        } else if (!added) {
            int drainResult = queue.drainTo(tempList, persistBatchSize);
            new PersistThread<T>(queue, tempList, new LogConfig(config.getProp())).start();
            tempList.clear();
        }
        String resultMsgStr = JSON.toJSONString(resultMsg, true);
        ctx.channel().writeAndFlush(resultMsgStr.getBytes());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
