package com.yudy.centaur.netty;

import com.google.protobuf.MessageLite;
import com.yudy.centaur.server.ServerConfig;
import com.yudy.centaur.server.ThreadManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class ProtobufNettyServerInitializer<T> extends ChannelInitializer<SocketChannel>{

    private final ThreadManager<T> threadManager;

    private final ServerConfig config;


    public ProtobufNettyServerInitializer(ThreadManager<T> threadManager, ServerConfig config) {
        this.threadManager = threadManager;
        this.config = config;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline=socketChannel.pipeline();

        pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(1048576,0,4,0,4));

        T t = (T)new Object();

        MessageLite ml=(MessageLite)t;

        ml.getDefaultInstanceForType();

        pipeline.addLast("protobufDecoder",new ProtobufDecoder(ml));

        //TODO


    }
}
