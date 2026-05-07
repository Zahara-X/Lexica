package org.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.example.client.handler.KeyboardHandler;
import org.example.client.handler.NettyHandler;
import org.example.data.GameData;

public class NettyClient {
    private final KeyboardHandler keyboardHandler;
    private final GameData gameData;
    public NettyClient(String host, int port, KeyboardHandler keyboardHandler, GameData gameData) {
        this.keyboardHandler = keyboardHandler;
        this.gameData = gameData;
        new Thread(() -> {
            NioEventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap strap = new Bootstrap();
                strap.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024 * 10, 0, 4, 0, 4));
                                ch.pipeline().addLast(new NettyHandler(gameData));
                            }
                        });
                ChannelFuture ch = strap.connect(host, port).sync();
                keyboardHandler.setChannel(ch.channel());
                ch.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        }).start();
    }
}