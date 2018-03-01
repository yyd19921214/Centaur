package com.yudy.centaur.producer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.Closeable;
import java.io.IOException;

public class NettyClient extends Thread implements Closeable{

    private final String host;

    private final int port;

    private EventLoopGroup group;



    public NettyClient(String host,int port){
        this.host=host;
        this.port=port;
    }

    @Override
    public void run() {
        group=new NioEventLoopGroup();
        Bootstrap b=new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new NettyClientInitializer());
        try {
            ChannelFuture f=b.connect(host,port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    @Override
    public void close() throws IOException {
        group.shutdownGracefully();
    }
}
