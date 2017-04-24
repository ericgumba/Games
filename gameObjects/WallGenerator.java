package gameObjects;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

/**
 * Created by ericgumba on 4/24/17.
 */
public class WallGenerator extends TankWorld {

  ArrayList<IndestructibleWall> invincibleWalls = new ArrayList();
  ArrayList<DestructibleWall> regularWalls = new ArrayList();
  int wallWidth = 38;


  WallGenerator() {
    int checkPoint = 0;
    for (int i = 0; i < 5 ; i++) {
      invincibleWalls.add(new IndestructibleWall(borderX/2, i * (borderY / 32)));
      checkPoint = i * borderY / 32;
    }
    for (int i = 0; i < 6; i++){
      invincibleWalls.add(new IndestructibleWall(borderX/2 - i * wallWidth, checkPoint));
    }

    for (int i = 0; i < 5; i++){
      invincibleWalls.add(new IndestructibleWall(borderX /2, borderY - 38 * i ));
      checkPoint = borderY - 38 * i;
    }

    for ( int i = 0; i < 5; i++){
      invincibleWalls.add(new IndestructibleWall(borderX/2 + i * wallWidth, checkPoint));
    }
    for ( int i = 0; i < 5; i++){
      invincibleWalls.add(new IndestructibleWall(38 + 38 * i, borderY/2 - 38 * 2));
    }
    checkPoint = 300;
    for (int i = 0 ; i < 13; i++){
      regularWalls.add(new DestructibleWall(borderX / 2, 300 + i * 38));
      regularWalls.add(new DestructibleWall(borderX / 2 - 38, 300 + i * 38 ));
      regularWalls.add(new DestructibleWall( borderX / 2 + 38 + 38 * i, 300));
      regularWalls.add(new DestructibleWall(borderX / 2 + 38 - 38 * i, 300 + 13 * 38) );
    }

  }

  public void draw(Graphics g, ImageObserver obs) {

    for (int i = 0; i < borderY / 32; i++) {
      IndestructibleWall outerXWall = new IndestructibleWall(borderX - 20, i * (borderY / 32));
      outerXWall.update();
      outerXWall.draw(g, obs);
      IndestructibleWall outerXWall2 = new IndestructibleWall(10, i * (borderY / 32));
      outerXWall2.update();
      outerXWall2.draw(g, obs);
    }
    for(int i = 0; i < borderX / 32; i++){
      IndestructibleWall outerYWallTop = new IndestructibleWall(i * borderX / 32 , 10);
      outerYWallTop.update();
      outerYWallTop.draw(g, obs);
      IndestructibleWall outerYWall2 = new IndestructibleWall(i * borderX / 32, borderY-20);
      outerYWall2.update();
      outerYWall2.draw(g, obs);
    }

    for (int i = 0; i < regularWalls.size(); i++) {
      regularWalls.get(i).update();
      regularWalls.get(i).draw(g, obs);
    }
    for ( int i = 0; i < invincibleWalls.size(); i++){
      invincibleWalls.get(i).update();
      invincibleWalls.get(i).draw(g, obs);
    }
  }

  public boolean collision(int oX, int oY, int oW, int oH) {
    for (int i = 0; i < regularWalls.size(); i++) {
      if (regularWalls.get(i).collision(oX, oY, oW, oH)) {
        return true;
      }
    }
    for(int i = 0; i < invincibleWalls.size(); i++) {
      if (invincibleWalls.get(i).collision(oX, oY, oW, oH)) {
        return true;
      }
    }
    return false;
  }

}
