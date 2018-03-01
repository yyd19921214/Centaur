package com.yudy.centaur.netty;

import com.yudy.centaur.server.ServerConfig;
import com.yudy.centaur.server.ThreadManager;
import com.yudy.centaur.util.Constance;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;

public class NettyServer<T> extends Thread implements Closeable{
    private static Logger LOG= Logger.getLogger(NettyServer.class);

    private final int port;
    private final EventLoopGroup bossGroup=new NioEventLoopGroup();
    private final EventLoopGroup workerGroup=new NioEventLoopGroup(1);


    private ThreadManager<T> threadManager;
    private ServerConfig config;
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
            ChannelHandler childHandler;
            if(logTransferType.equalsIgnoreCase(Constance.LOG_TYPE_PB))
            {
                childHandler=new ProtobufNettyServerInitializer<>(threadManager,config);
            }
            else if (logTransferType.equalsIgnoreCase(Constance.LOG_TYPE_JSON)){
                childHandler=new NettyServerInitializer<>(threadManager,config);
            }
            else{
                throw new IllegalArgumentException("不支持的日志格式");
            }
            b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).childHandler(childHandler);
            Channel ch;
            ch=b.bind(port).sync().channel();
            LOG.info("NettyServer启动"+port);
            ch.closeFuture().sync();



        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        LOG.warn("PHP日志服务器挂掉!!!");

    }

    @Override
    public void close() throws IOException {
        LOG.info("日志服务器关闭，释放占用端口"+port);
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

    }
}
