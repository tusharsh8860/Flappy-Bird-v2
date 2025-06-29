package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    
    //key variables
    public boolean upPressed;
    public boolean resetPressed = false;
    public boolean upJustPressed = false;
    public boolean resetJustPressed = false;
    public boolean pausePressed = false;
    public boolean pauseJustPressed = false;

    GamePanel gp;
    public KeyHandler(GamePanel gp)
    {
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER)
        {   
            if(!upPressed)
            {
                upJustPressed = true;
            }
            upPressed = true;
        }
        if(code == KeyEvent.VK_R && gp.gameState == gp.Game_Over_State)
        {
            if(!resetPressed)
            {
                resetJustPressed = true;
            }
            resetPressed = true;
        }
        if(code == KeyEvent.VK_P)
        {
            if(!pausePressed)
            {
                pauseJustPressed = true;
            }
            pausePressed = true;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

       if(code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER)
       {
            upPressed = false;
       }
       if(code == KeyEvent.VK_P)
        {
            pausePressed = false;
        }
       
    }
    public void consumeKeys()
    {
        upJustPressed = false;
        resetJustPressed = false;
        pauseJustPressed = false;
    }
}
