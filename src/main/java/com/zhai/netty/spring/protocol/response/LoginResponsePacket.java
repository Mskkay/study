package com.zhai.netty.spring.protocol.response;

import com.zhai.netty.spring.protocol.Packet;
import lombok.Data;

import static com.zhai.netty.spring.protocol.command.Command.LOGIN_RESPONSE;

@Data
public class LoginResponsePacket extends Packet {

    private String userId;

    private String userName;

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
