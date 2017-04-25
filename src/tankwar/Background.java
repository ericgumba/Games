package src.tankwar;

import static java.applet.Applet.newAudioClip;
import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;
import game.BackgroundInterface;

/**
 * @ SFSU Spring 2017 CSC413 assignment-4 4/18/2017
 */

public class Background extends TankWar implements BackgroundInterface {

    BufferedImage editableMap;
    Image map = getSprite("Resources/Backgroundfullbig2.png");
    URL url1 = TankWar.class.getResource("Resources/terminator_Theme.wav");
    AudioClip backgroundMusic = newAudioClip(url1);

    @Override
    public void draw(Graphics2D g, ImageObserver obs) {
        g.drawImage(map, 0, 0, obs);
    }

    @Override
    public void playMusic() {
        backgroundMusic.loop();
    }

    @Override 
    public void playGameOverMusic() {
            // implement all abstract methods
    }
}
