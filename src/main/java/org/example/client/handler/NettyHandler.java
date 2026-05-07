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
    private final GameData gameData;
    public NettyHandler(GameData gameData) {
        this.gameData = gameData;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
         int id = msg.readInt();
         if(id == 12) {
             if(msg.isReadable()) {
                 int w = msg.readInt();
                 int h = msg.readInt();
                 GameData.sizeMap = msg.readInt();
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
             if(hashId == gameData.hashMyId) {
                 gameData.cameraX = cameraX;
             } else {
                 PlayerDew data = gameData.instance.get(hashId);
                 if(data != null) {
                     data.x = cameraX;
                 } else {
                     PlayerDew prizrak = new PlayerDew();
                     prizrak.x = cameraX;
                     gameData.instance.put(hashId, prizrak);
                 }
             }
             logger.info("id: {}", hashId);
                 logger.info("cameraX: {}", cameraX);
         }
         if(id == 9) {
             int hashId = msg.readInt();
             int cameraY = msg.readInt();
             if(hashId == gameData.hashMyId) {
                 gameData.cameraY = cameraY;
             } else {
                 PlayerDew data = gameData.instance.get(hashId);
                 if(data != null) {
                     data.y = cameraY;
                 } else {
                     PlayerDew prizrak = new PlayerDew();
                     prizrak.y = cameraY;
                     gameData.instance.put(hashId, prizrak);
                 }
             }
             logger.info("cameraY: {}", cameraY);
         }
         if(id == 15) {
             int playerZ = msg.readInt();
             int cameraY = msg.readInt();
             int cameraX = msg.readInt();
             int hashId = msg.readInt();
             if(gameData.getId() == -1) {
                 gameData.id = 1;
                 gameData.playerZ = playerZ;
                 gameData.cameraX = cameraX;
                 gameData.cameraY = cameraY;
                 gameData.hashMyId = hashId;
             } else if(!gameData.instance.containsKey(hashId)) {
                 gameData.instance.put(hashId, new PlayerDew(cameraX, cameraY));
             }
         }
         if(id == 16) {
             int dropHash = msg.readInt();
             gameData.getInstance().remove(dropHash);
             logger.info("player disconnect: {}", dropHash);
         }
    }
}