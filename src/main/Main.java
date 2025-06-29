package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        
        window.setTitle("Flappy Bird Mac");
        
        //after game panel
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);

        gamePanel.paintImmediately(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

        window.setVisible(true);
        gamePanel.requestFocus();

       
        // gamePanel.startGameThread();
        
       
    }
}
