package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;

import Entity.Player;
import Obstacles.Pipes;
import Tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
    
    //Screen Variables
   final int originalTileSize = 16;
   final int scaleSize = 3; // 16*3 = 48 pixel character

   public final int TileSize = originalTileSize * scaleSize;

   public final int maxScreenColSize = 8;
   public final int maxScreenRowSize = 12;

   public int screenWidth = maxScreenColSize * TileSize;
   public int screenHeight = maxScreenRowSize * TileSize;
   
   //Game FPS and Score and pause state
   public int FPS = 60;
   private int score =0;
   private int highScore =0;
   public boolean paused = false;

   //Pipes
   int pipeSpawnTime =0;
   final int pipeSpawnInterval = 180;
//    public boolean gameOver = false; used this alot before gameStates to detect gameover and stop the game
   public ArrayList<Pipes> pipes = new ArrayList<>();

   //Game States
   public final int Title_State =0;
   public final int Play_State = 1;
   public final int Game_Over_State = 2; //NO need we're using gameOver instead
   public int gameState;                   // update we're using Game_Over_State now

   //Objects
   Thread gameThread;
   KeyHandler KeyH = new KeyHandler(this);
   Player player = new Player(this, KeyH);
   TileManager tileM = new TileManager(this);
   public Sound sound = new Sound();

   public GamePanel()
   {
        this.setPreferredSize(new DimensionUIResource(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.addKeyListener(KeyH);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.requestFocus();
        
        startGameThread();
        gameState = Title_State;
        sound.playMusic(3);
        
   }

   public void startGameThread()
   {    if(gameThread == null)
        {
            gameThread = new Thread(this);
            gameThread.start();
        }
   }

   @Override
   public void run() {
        
        double drawInterval = 1000000000/FPS;
        double nextDrawInterval = System.nanoTime() + drawInterval;
        double lastchecked = System.nanoTime();
        int frames =0;
        while(gameThread != null)
        {   //handleInput();
            // if(gameOver)
            // {               //game break condition
            //     break;
            // }
            handleInput();
            if(!paused && gameState == Play_State)
            {   
                update();
            }
            repaint();
            KeyH.consumeKeys();
            try {
                
                double remainingTime = nextDrawInterval - System.nanoTime();

                if(remainingTime<0)
                {
                    remainingTime =0;
                }
                Thread.sleep((long)remainingTime/1000000);

                frames++; //count frames

                nextDrawInterval += drawInterval;
                if(System.nanoTime() - lastchecked >= 1000000000)
                {   
                    System.out.println(frames);
                    lastchecked = System.nanoTime();
                    frames =0;
                }
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
   }
   //Centralized gameover
   public void triggerGameOver()
   {
        if(gameState == Play_State)
        {
            gameState = Game_Over_State;
            sound.PlaySe(1);
            sound.stop(3);
            if(score>highScore)
            {
                highScore = score;
            }
            System.out.println("Game Over");
        }
   }
//    handle all inputs
   public void handleInput()
   {
        if(KeyH.upJustPressed &&gameState == Title_State)
        {   
            resetGame();
            gameState = Play_State;
        }
        if(KeyH.pauseJustPressed && gameState == Play_State)
        {
            if(gameState == Play_State)
            {   
                sound.PlaySe(4);
                paused = !paused; 
            }
        } 
        if(KeyH.resetJustPressed && gameState == Game_Over_State)
        {   
            resetGame();
            KeyH.resetPressed = false;
        }
   }

   //reset game
   public void resetGame()
   {    
        player.setDefaults();
        pipes.clear();
        // pipes.add(new Pipes(this));
        pipeSpawnTime = 0;
        gameState = Title_State;
        score =0; 


        // reset sounds
        if(sound != null)
        {
            sound.stopAndCloseAllSounds();
        }
        sound = new Sound();
        sound.playMusic(3);

   }
   public void collision(Pipes pipe)
   {
        //check collision
            int playerLeft = player.x;
            int playerRight = player.x + player.playerWidth;
            int playerTop = player.y;
            int playerBottom = player.y + player.playerHeight;

            int topPipeLeft = pipe.x;
            int topPipeRight = pipe.x + pipe.pipeWidth;
            int topPipeTop = pipe.topPipeY- pipe.pipeHeight;
            int topPipeBottom = pipe.topPipeY;

            int bottomPipeLeft = pipe.x;
            int bottomPipeRight = pipe.x + pipe.pipeWidth;
            int bottomPipeTop = pipe.bottomPipeY;
            int bottomPipeBottom = pipe.bottomPipeY + pipe.pipeHeight;

            if(playerRight>topPipeLeft && playerTop < topPipeBottom 
                && playerBottom > topPipeTop && playerLeft< topPipeRight)
                {   
                    triggerGameOver();
                }
            if(playerRight>bottomPipeLeft && playerLeft<bottomPipeRight 
                && playerTop<bottomPipeBottom && playerBottom >bottomPipeTop)
                {   
                    triggerGameOver();
                }
   }
   public void update()
   {    
        if(gameState == Play_State)
        {   
            player.update();
            pipeSpawnTime++;
            if(pipeSpawnTime>pipeSpawnInterval)
            {
                pipes.add(new Pipes(this));
                pipeSpawnTime = 0;
            }
            for(int i =0; i<pipes.size(); i++)
            {
                Pipes pipe = pipes.get(i);
                pipe.update();

                collision(pipe);

                if(pipe.x + pipe.pipeWidth < 0)
                {   
                    score++;
                    sound.PlaySe(2);
                    pipes.remove(i);
                    i--; 
                }
            }  
        }
        
   }

   //title screen
   public void drawTitleScreen(Graphics2D g2)
   {
        g2.setColor(Color.YELLOW);
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD,40F));
        String Title = "FLAPPY BIRD";

        int x = screenWidth/2 -(int)g2.getFontMetrics().getStringBounds(Title, g2).getWidth() / 2;
        int y = screenHeight/3;

        g2.drawString(Title, x, y);

        String message = "Press Space To Play";
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.PLAIN,20F));
        x = screenWidth/2 -(int)g2.getFontMetrics().getStringBounds(message, g2).getWidth() / 2;
        y+= 50;
        g2.drawString(message, x, y);
   }

   @Override
   public void paintComponent(Graphics g)
   {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        tileM.draw(g2);// drap map firs

        //draw Title Screen
        if(gameState == Title_State)
        {
            drawTitleScreen(g2);
            g2.dispose();
            return;
        }
        if(paused)
        {   
            String pause = "PAUSED";
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 40F));
            int x = screenWidth/2 -(int)g2.getFontMetrics().getStringBounds(pause, g2).getWidth() / 2;
            int y = screenHeight/2;

            g2.drawString(pause, x, y);
        }
        for(Pipes pipe : pipes)
        {
            pipe.draw(g2);
        }
        player.draw(g2);//draw player 

        // score counter
        g2.setColor(Color.MAGENTA);
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 40F));
        String scoreText = String.valueOf(score);
        String highScoreText = "High Score: "+ String.valueOf(highScore);

        int x = screenWidth/2 -(int)g2.getFontMetrics().getStringBounds(scoreText, g2).getWidth() / 2;
        int y = TileSize;

        g2.drawString(scoreText, x, y);

        g2.setColor(Color.darkGray);
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.PLAIN, 15F));
        int hX = 0;
        int hY = TileSize-18;
        g2.drawString(highScoreText, hX, hY);

        if(gameState == Game_Over_State)
        {   
            g2.setColor(Color.red);
            g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD,60F));
            String gameOverString = "Game Over!";
            int Gx = screenWidth/2- (int)g2.getFontMetrics().getStringBounds(gameOverString, g2).getWidth() / 2;
            int Gy = TileSize*4;
            g2.drawString(gameOverString, Gx, Gy);

            String tryAgain = "Press R to try again";
            g2.setColor(Color.GRAY);
            g2.setFont(g2.getFont().deriveFont(java.awt.Font.PLAIN, 20F));

            int tX = screenWidth/2 - (int)g2.getFontMetrics().getStringBounds(tryAgain, g2).getWidth() / 2;
            int tY = TileSize*7;

            g2.drawString(tryAgain, tX, tY);
        }

        g2.dispose();
   }
}
