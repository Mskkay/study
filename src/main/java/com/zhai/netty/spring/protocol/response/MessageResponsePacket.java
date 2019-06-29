package com.zhai.netty.spring.protocol.response;

import com.zhai.netty.spring.protocol.Packet;
import lombok.Data;

import static com.zhai.netty.spring.protocol.command.Command.MESSAGE_RESPONSE;

@Data
public class MessageResponsePacket extends Packet {

    private String fromUserId;

    private String fromUserName;

    private String message;

    @Override
    public Byte getCommand() {

        return MESSAGE_RESPONSE;
    }
}
