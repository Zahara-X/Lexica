package org.example.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameData {
    public Map<Integer, PlayerDew> instance = new ConcurrentHashMap<>();
    public volatile int id = -1;
    public volatile int playerZ;
    public volatile int cameraX;
    public volatile int cameraY;
    public volatile int MyId;
    public volatile static int[][] grid;
    public volatile static int sizeMap;

    public int getId() {
        return id;
    }

    public int getCameraX() {
        return cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public int getPlayerZ() {
        return playerZ;
    }

    public Map<Integer, PlayerDew> getInstance() {
        return instance;
    }

    public GameData() {}



}