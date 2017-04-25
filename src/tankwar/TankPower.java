package src.tankwar;

import game.WallObjectInterface;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 * @ SFSU Spring 2017 CSC413 assignment 4 4/18/2017
 */


public class TankPower extends TankWar {
    Image icon;
    int x, y, width, height;
    
    TankPower(int x, int y) { 
        this.x = x;
        this.y = y;
    }

    public boolean update() {
        if(tankL.collision(x, y, width, height)) {
            return true;
        } else if(tankR.collision(x, y, width, height)) {
            return true;
        } else {
            return false;
        }
    }

    public void draw(Graphics g, ImageObserver obs) {
        g.drawImage(icon, x, y, obs);
    }
}
