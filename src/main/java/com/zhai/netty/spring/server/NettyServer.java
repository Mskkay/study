package com.zhai.netty.spring.server;

import com.zhai.netty.spring.codec.PacketDecoder;
import com.zhai.netty.spring.codec.PacketEncoder;
import com.zhai.netty.spring.codec.Spliter;
import com.zhai.netty.spring.server.handler.AuthHandler;
import com.zhai.netty.spring.server.handler.LoginRequestHandler;
import com.zhai.netty.spring.server.handler.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Administrator
 */
@Component
public class NettyServer {

    private static final int PORT = 8000;


    public static void main(String[] args) {
        new NettyServer().run();
    }


    /**
     * @date 2019/6/30 0030 9:53
     * @author Administrator
     * @description 启动run
     */
    public void run() {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new AuthHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });


        bind(serverBootstrap, PORT);
    }


    /**
     * @param serverBootstrap 服务端框架
     * @param port            监听端口
     * @date 2019/6/30 0030 9:53
     * @author Administrator
     * @description 绑定端口
     */
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }
}
