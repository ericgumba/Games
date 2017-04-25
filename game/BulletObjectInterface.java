package game;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * @ SFSU Spring 2017 CSC413 assignment 4 4/18/2017
 */

public interface BulletObjectInterface {
    public boolean collision(int x, int y, int w, int h);
    boolean move();
    public void draw(Graphics g, ImageObserver obs);
}
