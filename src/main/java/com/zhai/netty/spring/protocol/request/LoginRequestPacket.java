package com.zhai.netty.spring.protocol.request;

import com.zhai.netty.spring.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.zhai.netty.spring.protocol.command.Command.LOGIN_REQUEST;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRequestPacket extends Packet {

    private String userName;

    private String password;

    @Override
    public Byte getCommand() {

        return LOGIN_REQUEST;
    }
}
