package game;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

/**
 * @ SFSU Spring 2017 CSC413 assignment 4 4/17/2017
 */

//only usertank and enemy tank need implement this
public interface MobileObjectInterface extends Observer { 
    public void move();
    public void draw(Graphics g, ImageObserver obs);
    public void update(Observable obj, Object arg);
}
