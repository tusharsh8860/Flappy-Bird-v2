package Tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
    
    GamePanel gp;

    //tile and map variables
    Tile tile[]; //tile array
    int mapTileNum[][];//map 

    public TileManager(GamePanel gp)
    {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenRowSize][gp.maxScreenColSize];//create matrix


        getTileImage();
        loadMap("/Maps/map01.txt");
    }
    public void getTileImage()
    {
        try 
        {   tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/Clouds2.png"));
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/Sky2.png"));
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/Ground2.png"));
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/upperWall2.png"));
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    public void loadMap(String load)
    {   InputStream is;
        BufferedReader br;
        try 
        {
            is = getClass().getResourceAsStream(load);
            br = new BufferedReader(new InputStreamReader(is));

            int row =0;
            int col =0;

            while(row < gp.maxScreenRowSize)
            {
                String line  = br.readLine();
                if(line == null)
                {
                    System.out.println("invalid map");
                    break;
                }
                col =0;
                while(col<gp.maxScreenColSize)
                {   
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[row][col] = num;
                    col++;
                }
                row++;
            }
            
            br.close();

        } catch (Exception e) 
        {
            e.printStackTrace();
        }  
    }
    public void draw(Graphics2D g2)
    {
        int col =0;
        int row =0;
        int x =0;
        int y =0;

        while(row<gp.maxScreenRowSize)
        {   
            while(col<gp.maxScreenColSize)
            {   
                int tileNum = mapTileNum[row][col];
                g2.drawImage(tile[tileNum].image, x, y,gp.TileSize, gp.TileSize, null);
                x += gp.TileSize;
                col++;
            }
            row++;
            y+= gp.TileSize;
            col =0;
            x = 0;
            
        }
    }

}
