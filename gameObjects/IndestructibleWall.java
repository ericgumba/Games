package gameObjects;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Created by ericgumba on 4/23/17.
 */
public class IndestructibleWall extends TankWorld {

  Image wallImage = imageGenerator.getImage("Images/Blue_wall1.png");
  int x, y, width, height;
  boolean isVisible;

  IndestructibleWall(int x, int y) {
    this.x = x;
    this.y = y;
    width = wallImage.getWidth(null);
    height = wallImage.getHeight(null);
    isVisible = true;
  }

  public void update() {
    for ( Bullet bullet : tankOneBullets )
      if ( bullet.collision( x + 20, y, width - 20, height )){
        // add sound generation later.
      }
    for ( Bullet bullet : tankTwoBullets ) {
      if (bullet.collision(x + 20, y, width - 20, height)) {
        // add sound generation later.
      }
    }
  }

  public void draw(Graphics g, ImageObserver obs) {
    if (isVisible) {
      g.drawImage(wallImage, x, y, observer);
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
