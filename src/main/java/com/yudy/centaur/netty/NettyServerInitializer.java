package com.yudy.centaur.netty;

import com.yudy.centaur.server.ServerConfig;
import com.yudy.centaur.server.ThreadManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

//import java.nio.channels.SocketChannel;

public class NettyServerInitializer<T> extends ChannelInitializer<SocketChannel> {

    private final ThreadManager<T> threadManager;
    private final ServerConfig config;

    public NettyServerInitializer(ThreadManager<T> threadManager, ServerConfig config) {
        this.threadManager = threadManager;
        this.config = config;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline= socketChannel.pipeline();

        pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(1048576,0,4,0,4));
        //TODO
//        pipeline.addLast("handler",new )



    }
}
