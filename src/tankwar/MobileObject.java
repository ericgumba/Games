package src.tankwar;

import game.GameEvents;
import game.MobileObjectInterface;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Observable;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  @ SFSU Spring 2017  CSC413 assignment 44/18/2017
 */

public class MobileObject extends TankWar implements MobileObjectInterface {
    
    ArrayList<TankPower> powers = new ArrayList();
    ArrayList<Wall> allWalls = new ArrayList();

     MobileObject() {}

    public void addWall (int x, int y, char wallType) {
            allWalls.add(new Wall(x,y,wallType));
    }
    
    
    @Override
    public void move() {
        //  implement all abstract methods
    }

    @Override
    public void draw(Graphics g, ImageObserver obs) {
        for (int i = 0; i < powers.size(); i++) {
            if (!powers.get(i).update()) {
                powers.get(i).draw(g, obs);
            } else {
                powers.remove(i);
            }
        }

        for (int i = 0; i < allWalls.size(); i++) {
            allWalls.get(i).update();
            allWalls.get(i).draw(g, obs);
        }
    }

    public boolean collision(int oX, int oY, int oW, int oH) {
        for (int i = 0; i < allWalls.size(); i++) {
            if (allWalls.get(i).collision(oX, oY, oW, oH)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(Observable obj, Object event) {
        GameEvents gameE = (GameEvents) event;
    }
}
