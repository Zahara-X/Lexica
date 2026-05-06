package org.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.example.client.handler.KeyboardHandler;
import org.example.client.handler.NettyHandler;

public class NettyClient {
    public NettyClient(String host, int port) {
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
                                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4));
                                ch.pipeline().addLast(new NettyHandler());
                            }
                        });
                ChannelFuture ch = strap.connect(host, port).sync();
                KeyboardHandler.setChannel(ch.channel());
                ch.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        }).start();
    }
}