package com.zhai.netty.spring;

import com.zhai.netty.spring.server.NettyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Administrator
 * @date 2019/6/30 0030
 * @description
 */
public class WebApp {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        NettyServer timeServer=  ac.getBean(NettyServer.class);
        timeServer.run();
    }
}
