package org.example.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameData {
    public final Map<Integer, PlayerDew> instance = new ConcurrentHashMap<>();
    public volatile static int id = -1;
    public volatile static int hashMyId;
    public volatile static int[][] grid;
    public volatile int cameraX;
    public volatile int cameraY;
    public volatile static int playerZ;

    public int getCameraX() {
        return cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public Map<Integer, PlayerDew> getInstance() {
        return instance;
    }

}