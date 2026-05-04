package org.example.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.data.GameData;
import org.example.data.PlayerDew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static Logger logger = LoggerFactory.getLogger(NettyHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
         int id = msg.readInt();
         if(id == 12) {
             if(msg.isReadable()) {
                 int w = msg.readInt();
                 int h = msg.readInt();
                 int[][] newGrid = new int[w][h];
                 for (int i = 0; i < w; i++) {
                     for (int j = 0; j < h; j++) {
                         newGrid[i][j] = msg.readInt();
                     }
                 }
                GameData.grid = newGrid;
             }
         }
         if(id == 8) {
             int hashId = msg.readInt();
             int cameraX = msg.readInt();
             logger.info("hashX: {}", hashId);
             if(GameData.hashMyId == hashId) {
                 GameData.cameraX = cameraX;
             } else {
                 PlayerDew data = GameData.instance.get(hashId);
                 if(data != null) data.x = cameraX;
             }
                 logger.info("cameraX: {}", cameraX);
         }
         if(id == 9) {
             int hashId = msg.readInt();
             int cameraY = msg.readInt();
             logger.info("hashY: {}", hashId);
             if(GameData.hashMyId == hashId) {
                 GameData.cameraY = cameraY;
             } else {
                 PlayerDew data = GameData.instance.get(hashId);
                 if(data != null) data.y = cameraY;

             }
             logger.info("cameraY: {}", cameraY);
         }
         if(id == 15) {
             int playerZ = msg.readInt();
             int cameraY = msg.readInt();
             int cameraX = msg.readInt();
             int hashId = msg.readInt();
             if(GameData.id == -1) {
                 GameData.playerZ = playerZ;
                 GameData.cameraX = cameraX;
                 GameData.cameraY = cameraY;
                 GameData.hashMyId = hashId;
                 logger.info("x: {}, y: {}, hash: {}, playerZ: {}", cameraX,  cameraY, hashId, playerZ);
             }
         }
    }
}