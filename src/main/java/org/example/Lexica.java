package org.example;
import org.example.client.NettyClient;
import org.example.client.handler.KeyboardHandler;
import org.example.data.GameData;
import org.example.data.PlayerDew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class Lexica extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(Lexica.class);
    private static final KeyboardHandler keyboard = new KeyboardHandler();
    private static final String TITLE = "Lexica";
    private static final int[] entity = new int[10];
    private static final boolean[] keys = new boolean[256];
    public Lexica() {
    this.setBackground(Color.BLACK);
    // Keyboard focus is active
    this.setFocusable(true);
    this.requestFocusInWindow();
    this.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {keys[e.getKeyCode()] = true;}
        @Override
        public void keyReleased(KeyEvent e) {keys[e.getKeyCode()] = false;}
    });
    new Timer(16, e -> {
        this.updateUI();
        if(keys[KeyEvent.VK_W]) keyboard.keyboard((byte)2);
        if(keys[KeyEvent.VK_S]) keyboard.keyboard((byte)5);
        if(keys[KeyEvent.VK_A]) keyboard.keyboard((byte)7);
        if(keys[KeyEvent.VK_D]) keyboard.keyboard((byte)9);
//        logger.info("camera: {}", GameData.cameraX);
        this.repaint();
    }).start();
    GameData.instance.put(GameData.hashMyId, new PlayerDew(GameData.cameraX, GameData.cameraY));
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if(GameData.grid == null) {
            g2.setColor(Color.RED);
            g2.drawString("Загрузка...", 20,20);
            return;
        }
        int size = 150;
        entity[0] = GameData.grid.length * size;
        entity[1] = GameData.grid[0].length * size;
        entity[2] = (this.getWidth() - entity[0]) / 0x2;
        entity[3] = (this.getHeight() - entity[1]) / 0x2;
        g2.setColor(Color.WHITE);
        for (int i = 0; i < GameData.grid.length; i++) {
            for (int j = 0; j < GameData.grid[i].length; j++) {
                entity[4] = (i * size) + entity[2] - GameData.cameraX;
                entity[5] = (j * size) + entity[3] - GameData.cameraY;
                g2.drawRect(entity[4], entity[5], size, size);
            }
        }
        int player = GameData.playerZ;
        entity[6] = (this.getWidth() - player) / 0x2;
        entity[7] = (this.getHeight() - player) / 0x2;
        for(PlayerDew data : GameData.instance.values()) {
            entity[8] = (data.x - GameData.cameraX) + entity[6];
            entity[9] = (data.y - GameData.cameraY) + entity[7];
            g2.fillOval(entity[8], entity[9], player, player);
        }
        g2.setColor(Color.WHITE);
        g2.fillOval(entity[6], entity[7], player, player);
    }
    public static void main(String[] args) {
       JFrame window = new JFrame(TITLE);
       window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       window.setSize(new Dimension(0x3e8,0x258));
       window.setLocationRelativeTo(null);
       window.add(new Lexica());
       window.setVisible(true);
       new NettyClient("localhost", 8080);

    }
}
