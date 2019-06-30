package com.zhai.netty.spring.util;

import com.zhai.netty.spring.attribute.Attributes;
import com.zhai.netty.spring.session.Session;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 */
public class SessionUtil {

    /**
     * 这里是把所有登陆的用户用userID为主键存入一个map之中
     */
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        //登陆这里的属性绑定是使用的channel.attr的方法绑定到channel之中。
        // Attributes.SESSION是我们自己定义
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {

        return channel.hasAttr(Attributes.SESSION);
    }

    public static Session getSession(Channel channel) {

        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {

        return userIdChannelMap.get(userId);
    }

    /**
     * @return int
     * @date 2019/6/30 0030 10:20
     * @author Administrator
     * @description 当前用户数量
     */
    public static int currentUserSize() {

        return userIdChannelMap.size();
    }
}
