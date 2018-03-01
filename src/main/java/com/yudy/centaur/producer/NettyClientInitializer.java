package com.yudy.centaur.producer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

//import java.nio.channels.SocketChannel;
import java.util.logging.SocketHandler;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel>{

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline=socketChannel.pipeline();
        pipeline.addLast("frameEncoder",new LengthFieldPrepender(4));
        pipeline.addLast("protobufEncoder",new ProtobufEncoder());
        pipeline.addLast("protobufHandler",new NettyClientHandler<>());
    }
}
