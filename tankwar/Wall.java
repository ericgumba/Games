package tankwar;

import game.WallObjectInterface;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 * @ SFSU Spring 2017 CSC413 assignment 4 4/18/2017
 */

public class Wall extends TankWar implements WallObjectInterface {

    Image wallImage1 = getSprite("Resources/Blue_wall1.png");
    Image wallImage2 = getSprite("Resources/Blue_wall2.png");
    int x, y, width, height;
    //int invisibleTime, timeCounter;  //  reserved for game 2 code
    char wallType; 
    boolean isVisible;

    Wall(int x, int y, char wallType) {
        this.x = x;
        this.y = y;
        this.wallType = wallType;
        width = wallImage2.getWidth(null);
        height = wallImage2.getHeight(null);
        isVisible = true;
        //invisibleTime = 600;    //  reserved for game 2 code
        //timeCounter = 0;        //  reserved for game 2 code
        //System.out.println ("Wall width " + width + " Wall height "+height);
    }

    @Override
    public void update() {
        if (isVisible) {
            for (int i = 0; i < tankLBullets.size(); i++) {
                if (tankLBullets.get(i).collision(x + 20, y, width - 20, height)) {
                    if (wallType=='2') isVisible = false; // only wallType='2' can be descroyed
                    explosionSound_2.play();
                }
            }
            for (int i = 0; i < tankRBullets.size(); i++) {
                if (tankRBullets.get(i).collision(x + 20, y, width - 20, height)) {
                    if (wallType=='2') isVisible = false; // only wallType='2' can be descroyed
                    explosionSound_2.play();
                }
            }
        } /*else {
            timeCounter++;
            if ((timeCounter >= invisibleTime) && (!Enemy[1].collision(x, y, width, height)) && (!Enemy[2].collision(x, y, width, height))) {
                isVisible = true;
                timeCounter = 0;
            }
        } */
    }

    @Override
    public void draw(Graphics g, ImageObserver obs) {
        if (isVisible) {
            if(wallType=='1'){   // wall type 1 un-destructable wall
                 g.drawImage(wallImage1, x, y, observer);
            } else if(wallType=='2') {    // wall type 2 destructable wall
                 g.drawImage(wallImage2, x, y, observer);
            }

        }
    }

    public boolean collision(int oX, int oY, int oW, int oH) {
        if (isVisible) {     
            if ((oY + oH > this.y) && (oY < this.y + height)) {
                if ((oX + oW > this.x) && (oX < this.x + width)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
