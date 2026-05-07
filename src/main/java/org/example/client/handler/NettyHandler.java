package org.example.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.data.GameData;
import org.example.data.PlayerDew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Logger logger = LoggerFactory.getLogger(NettyHandler.class);
    private final GameData gameData;
    public NettyHandler(GameData gameData) {
        this.gameData = gameData;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) {
         int id = buf.readInt();
         if(id == 12) _0230323_map_(buf);
         if(id == 8) _0003210_cameraX_(buf);
         if(id == 9) _0003210_cameraY_(buf);
         if(id == 15) _0000111_add_(buf);
         if(id == 16) _0003292_drop_(buf);
    }
    public void _0000111_add_(ByteBuf buf) {
        /// Spawn игрока на карте
        long PACKET = buf.readLong();
        long playerZ = (PACKET >> 12) & 0xFFF;
        long cameraX = (PACKET >> 24) & 0xFFF;
        long cameraY = (PACKET >> 36) & 0xFFF;
        long MyId = PACKET & 0xFFF;
        if (gameData.getId() == -1) {
            gameData.id = 1;
            gameData.playerZ = (int) playerZ;
            gameData.cameraX = (int) cameraX;
            gameData.cameraY = (int) cameraY;
            gameData.MyId = (int) MyId;
        } else if (gameData.instance.containsKey((int) MyId)) {
            gameData.getInstance().put((int) MyId, new PlayerDew((int) cameraX, (int) cameraY));
        }
    }
    public void _0003292_drop_(ByteBuf buf) {
        /// Удаляем игрока, если он вышел из сети
        int drop = buf.readInt();
        gameData.getInstance().remove(drop);
        logger.info("drop player: {}", drop);
    }
    public void _0003210_cameraX_(ByteBuf buf) {
        /// Управление X
        int Id = buf.readInt();
        int cameraX = buf.readInt();
        if(Id == gameData.MyId) {
            gameData.cameraX = cameraX;
        } else {
            PlayerDew data = gameData.instance.get(Id);
            if(data != null) { // проверяем, если игрока нет в списке, убираем фантома
                data.x = cameraX;
            } else {
                PlayerDew dew = new PlayerDew();
                dew.x = cameraX;
                gameData.instance.put(Id, dew);
            }
        }
        logger.info("cameraX: {}", cameraX);
    }
    public void _0003210_cameraY_(ByteBuf buf) {
        /// Управление Y
        int Id = buf.readInt();
        int cameraY = buf.readInt();
        if(Id == gameData.MyId) {
            gameData.cameraY = cameraY;
        } else {
            PlayerDew data = gameData.instance.get(Id);
            if(data != null) { // проверяем, если игрока нет в списке, убираем фантома
                data.y = cameraY;
            } else {
                PlayerDew dew = new PlayerDew();
                dew.y = cameraY;
                gameData.instance.put(Id, dew);
            }
        }
        logger.info("cameraY: {}", cameraY);
    }
    public void _0230323_map_(ByteBuf buf) {
        int PACKET = buf.readInt();
        int w = (PACKET >> 8) & 0xFF;
        int h = (PACKET >> 16) & 0xFF;
        GameData.sizeMap = (PACKET & 0xFF);
        int[][] newGrid = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                newGrid[i][j] = buf.readInt();
            }
        }
        GameData.grid = newGrid;
    }
}