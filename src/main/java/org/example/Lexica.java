package org.example;
import org.example.client.NettyClient;
import org.example.client.handler.KeyboardHandler;
import org.example.client.handler.NettyHandler;
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
    private static final int[][] grid = new int[32][32];
    private static final int[] entity = new int[8];
    private static final boolean[] keys = new boolean[256];
    private static final int randomSpawn = (int)(Math.random() * 2365);
    private static int cameraX = randomSpawn, cameraY = randomSpawn;
    private static final int speed = 12;
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

        if(cameraY <= -2365) cameraY = -2365;
        if(cameraY >= 2365) cameraY = 2365;
        if(cameraX <= -2365) cameraX = -2365;
        if(cameraX >= 2365) cameraX = 2365;
        this.repaint();
    }).start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int size = 150;
        entity[0] = grid.length * size;
        entity[1] = grid[0].length * size;
        entity[2] = (this.getWidth() - entity[0]) / 0x2;
        entity[3] = (this.getHeight() - entity[1]) / 0x2;
        g2.setColor(Color.WHITE);
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                entity[4] = entity[2] + (i * size) - cameraX;
                entity[5] = entity[3] + (j * size) - cameraY;
                g2.drawRect(entity[4], entity[5], size, size);
            }
        }
        int player = 64;
        entity[6] = (this.getWidth() - player) / 0x2;
        entity[7] = (this.getHeight() - player) / 0x2;
        g2.setColor(Color.WHITE);
        g2.fillOval(entity[6], entity[7], player, player);
    }
    public static void main(String[] args) {
        new NettyClient("localhost", 8080);
       JFrame window = new JFrame(TITLE);
       window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       window.setSize(new Dimension(0x3e8,0x258));
       window.setLocationRelativeTo(null);
       window.add(new Lexica());
       window.setVisible(true);

    }
}
