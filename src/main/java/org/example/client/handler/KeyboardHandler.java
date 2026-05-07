package org.example.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class KeyboardHandler {
    private Channel channel;
    private ByteBuf buffer;
    // делаем инициализацию
    public void setChannel(Channel ch) {
        this.channel = ch; // присваиваем к текущему (channel)
    }
    // Метод, отправителя, тип клавиатура, ну тут и так понятно что это keyboard :-)
    public void keyboard(byte key) {
        if(channel != null && channel.isActive()) {
            buffer = channel.alloc().buffer(1);
            buffer.writeByte(key);
            channel.writeAndFlush(buffer);
        } else throw new IllegalArgumentException("Cannot initialized!");
    }
}