package org.example.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static ByteBuf buffer;
    private static Logger logger = LoggerFactory.getLogger(NettyHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
         int id = msg.readInt();
         if(id == 12) {
             int width = msg.readInt();
             int height = msg.readInt();
             int[][] newGrid = new int[height][width];
             for(int i = 0; i < newGrid.length; i++) {
                 for(int j = 0; j < newGrid[i].length; j++) {
                     newGrid[i][j] = msg.readInt();
                 }
             }
         }
         if(id == 4) {
             if(msg.isReadable()) {
                 int cameraX = msg.readInt();
                 logger.info("camera: {}", cameraX);
             }

         }
         if(id == 5) {
             int playerSpawn = msg.readInt();
             logger.info("playerSpawn: {}", playerSpawn);
         }
    }
}