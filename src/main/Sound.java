package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    
    //sound variables
    URL soundURL[] = new URL[5];
    Clip soundClips[] = new Clip[5];

    public Sound()
    {
        soundURL[0] = getClass().getResource("/Sound/wing2.wav");
        soundURL[1] = getClass().getResource("/Sound/hit2.wav");
        soundURL[2] = getClass().getResource("/Sound/point2.wav");
        soundURL[3] = getClass().getResource("/Sound/music2.wav");
        soundURL[4] = getClass().getResource("/Sound/pause.wav");

        loadSound(0);
        loadSound(1);
        loadSound(2);
        loadSound(3);
        loadSound(4);
    }
    public void loadSound(int i)
    {
        try 
        {   if(soundURL[i] == null)
            {
                System.out.println("sound not found: " + i);
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            soundClips[i] = AudioSystem.getClip();//create a empty clip
            soundClips[i].open(ais);
        } 
        catch (Exception e) 
        {   
            System.out.println("clip not found: "+ soundURL[i]);
            e.printStackTrace();
        }
    }
    public void PlaySe(int i)
    {
        if(soundClips[i] != null)
        {   
            if(soundClips[i].isRunning())
            {
                soundClips[i].stop();
            }  
            soundClips[i].setFramePosition(0);
            soundClips[i].start();
        }
    }
    public void playMusic(int i)
    {
        if(soundClips[i] != null)
        {
            if(soundClips[i].isRunning())
            {
                soundClips[i].stop();
            }
            soundClips[i].setFramePosition(0);
            soundClips[i].loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    public void stop(int i)
    {
        if(soundClips[i] != null && soundClips[i].isRunning())
        {
            soundClips[i].stop();
        }
    }
    // close all sounds on reset
    public void stopAndCloseAllSounds()
    {
        for(int i =0; i<soundClips.length; i++)
        {
            if(soundClips[i] != null && soundClips[i].isOpen())
            {
                soundClips[i].stop();
                soundClips[i].close();
                soundClips[i] = null;
            }
        }
    }

}
