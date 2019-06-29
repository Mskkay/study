package com.zhai.netty.spring.protocol.request;

import com.zhai.netty.spring.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.zhai.netty.spring.protocol.command.Command.MESSAGE_REQUEST;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MessageRequestPacket extends Packet {
    private String toUserId;
    private String message;

    public MessageRequestPacket(String toUserId, String message) {
        this.toUserId = toUserId;
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
