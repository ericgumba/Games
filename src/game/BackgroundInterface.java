package src.game;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

/**
 * @ SFSU Spring 2017 CSC413 assignment-4 4/18/2017
 */

public interface BackgroundInterface {
    void draw(Graphics2D g, ImageObserver obs);
    void playMusic();
    void playGameOverMusic();
}
