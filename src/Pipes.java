package Obstacles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Pipes extends Obstacle{
    //pipe variables
    public int pipeHeight;
    public int pipeWidth;
    public int topPipeY;
    public int bottomPipeY;
    public final int gapHeight = 150;
    public final int motion = 1;

    public boolean passed = false;
    public final int initialX;

    //pipe image
    private static BufferedImage topImage;
    private static BufferedImage bottomImage;

    GamePanel gp;

    public Pipes(GamePanel gp)
    {
        this.gp = gp;
        this.initialX = gp.TileSize + gp.screenWidth;
        this.pipeWidth = (int)(gp.TileSize*1.5);
        this.pipeHeight = gp.screenHeight;
        setDefaults();
        getImage();
    }

    public void setDefaults()
    {   
        x = initialX;
        y = 0;
        initialPosition();
        this.passed = false;
        this.collision = true;
    }
    public void initialPosition()
    {
        Random random = new Random();

        //minimum gap
        int minYGap = (int)(gp.TileSize* 2.5);
        //max gap 
        int maxYGap = gp.screenHeight - (int)(gp.TileSize* 2.5) - gapHeight;

        if(maxYGap < minYGap)
        {
            maxYGap = minYGap;
        }
        int gapFromBorder = random.nextInt(maxYGap-minYGap+1) + minYGap;
        this.topPipeY = gapFromBorder;
        this.bottomPipeY = gapFromBorder + gapHeight;

    }   
    public void getImage()
    {
        try 
        {
            
            topImage = ImageIO.read(getClass().getResourceAsStream("/Pipes/top2.png"));
            bottomImage = ImageIO.read(getClass().getResourceAsStream("/Pipes/pipe2.png"));

        } catch (Exception e) 
        {
           e.printStackTrace();
        }
    }
    public void update()
    {
        x -= motion;
        // if(x + pipeWidth < 0)
        // {
        //     setDefaults();
        // } casues overlapping pipes
    }
    public void draw(Graphics2D g2)
    {   
        if(topImage != null & bottomImage != null)
        {
            g2.drawImage(topImage, x, topPipeY - pipeHeight, pipeWidth, pipeHeight, null);
            g2.drawImage(bottomImage, x, bottomPipeY, pipeWidth, pipeHeight, null);
        }
        else
        {
            g2.setColor(Color.green);
            g2.fillRect(x, topPipeY - pipeHeight, pipeWidth, pipeHeight);
            g2.fillRect(x, bottomPipeY, pipeWidth, pipeHeight);
        }
    }

}
