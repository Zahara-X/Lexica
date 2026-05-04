package org.example.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameData {
   public static final Map<Integer, PlayerDew> instance = new ConcurrentHashMap<>();
   public volatile static int id = -1;
   public volatile static int hashMyId;
   public volatile static int[][] grid;
   public volatile static int cameraX;
   public volatile static int cameraY;
   public volatile static int playerZ;

}