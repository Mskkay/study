package com.zhai.netty.server.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

/**
 * @author Administrator
 * @date 2019/6/17 0017
 * @description 一个server的demo
 */
public final class Server {

    public static void main(String[] args) throws Exception {
        /*
         * 首先生成两个nioEventLoopGroup
         * bossGroup将会承担事件收集器的角色
         * workerGroup则是负责监听整个事件收集器将事件分配给线程处理
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            //这里是一些服务器的参数等等的配置
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue")
                    .handler(new ServerHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        /**
                         * @param ch    netty包装后的socketChannel
                         * @date 2019/6/17 0017 16:16
                         * @author Administrator
                         * @description 添加各种操作的方法
                         */
                        @Override
                        public void initChannel(SocketChannel ch) {

                            /*
                             * 这里可以查看pipeline，ChannelPipeline采用了责任链模式，在使用的时候则是将时间处理的逻辑所在的类注册到这里
                             */
                            ch.pipeline().addLast();
                        }
                    });

            //这一步启动了netty服务器，并且绑定在了8888端口（看起来像是socket写法一样）
            ChannelFuture f = b.bind(8888).sync();

            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
