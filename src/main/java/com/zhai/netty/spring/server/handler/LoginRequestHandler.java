package com.zhai.netty.spring.server.handler;

import com.zhai.netty.spring.protocol.request.LoginRequestPacket;
import com.zhai.netty.spring.protocol.response.LoginResponsePacket;
import com.zhai.netty.spring.session.Session;
import com.zhai.netty.spring.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUserName(loginRequestPacket.getUserName());

        if (valid(loginRequestPacket)) {
            loginResponsePacket.setSuccess(true);
            String userId = randomUserId();
            loginResponsePacket.setUserId(userId);
            System.out.println("[" + loginRequestPacket.getUserName() + "]登录成功");
            SessionUtil.bindSession(new Session(userId, loginRequestPacket.getUserName()), ctx.channel());
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }

        // 登录响应
        ctx.channel().writeAndFlush(loginResponsePacket);

        System.out.println("现有连接数为" + SessionUtil.currentUserSize());
    }


    /**
     * @param loginRequestPacket 登陆用的信息包
     * @return boolean
     * @date 2019/6/30 0030 10:11
     * @author Administrator
     * @description 这个是验证帐号的方法。当然demo里面来者不拒
     */
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    private static String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println(SessionUtil.getSession(ctx.channel()).getUserName() + "断开连接！");
        SessionUtil.unBindSession(ctx.channel());
        System.out.println("现有连接数为" + SessionUtil.currentUserSize());
    }
}
