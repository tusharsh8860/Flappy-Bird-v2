package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.Sound;

public class Player extends Entity{
    
    GamePanel gp;
    KeyHandler keyH;
    Sound sound = new Sound();
    
    //player variables
    public final int playerWidth = 30;
    public final int playerHeight = 30;

    public Player(GamePanel gp, KeyHandler keyH)
    {
        this.gp = gp;
        this.keyH = keyH;
        setDefaults();
        getImage();
    }
    public void setDefaults()
    {
        x = 10;
        y = 200;
        jumpStrength = -12;
        yVelocity =0;
        gravity =1;
    }
    public void getImage()
    {
       try 
       {
            
        bird1 = ImageIO.read(getClass().getResourceAsStream("/Player/Yellow1.png"));
        bird2 = ImageIO.read(getClass().getResourceAsStream("/Player/Yellow2.png"));

       } 
       catch (Exception e) {
            e.printStackTrace();
       }
    }   
    public void update()
    {   
        
        if(keyH.upJustPressed)
        {   
            sound.PlaySe(0);
            yVelocity = jumpStrength;
        }
        yVelocity += gravity;
        y += yVelocity;
        int groundLevel = gp.screenHeight - gp.TileSize;
        
        if(y <= 0)
        {
            y =0;
            yVelocity =0;
            gp.triggerGameOver();
            // gp.gameState = gp.Game_Over_State;
        }
        else if(y >= groundLevel)
        {
            y = groundLevel;
            yVelocity=0;
            gp.triggerGameOver();
        }
        spiteCounter++;
        if(spiteCounter>15)
        {
            if(spiteNum == 1)
            {
                spiteNum =2;
            }
            else if(spiteNum ==2)
            {
                spiteNum = 1;
            }
            spiteCounter =0;
        }
    }
    public void draw(Graphics2D g2)
    {
        // g2.setColor(Color.yellow);
        // g2.fillRect(x, y, gp.TileSize, gp.TileSize);

        BufferedImage image = null;
        switch(spiteNum)
        {
            case 1:
                {
                    image = bird1;
                    break;
                }
            case 2:
                {
                    image= bird2;
                    break;
                }
        }
        if(image != null)
        {
            g2.drawImage(image, x, y, gp.TileSize, gp.TileSize, null);
        }
        else
        {
            g2.setColor(Color.yellow);
            g2.fillRect(x, y, playerWidth, playerHeight);
        }
    }

}
