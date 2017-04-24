package gameObjects;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Created by ericgumba on 4/23/17.
 */
public class DestructibleWall extends TankWorld {
  Image wallImage = imageGenerator.getImage("Images/Blue_wall2.png");
  int x, y, width, height;
  boolean isVisible;

  DestructibleWall(int x, int y) {
    this.x = x;
    this.y = y;
    width = wallImage.getWidth(null);
    height = wallImage.getHeight(null);
    isVisible = true;
  }

  public void update() {
    if (isVisible) {
      for (int i = 0; i < tankLBullets.size(); i++) {
        if (tankLBullets.get(i).collision(x + 20, y, width - 20, height)) {
          isVisible = false;
        }
      }
      for (int i = 0; i < tankRBullets.size(); i++) {
        if (tankRBullets.get(i).collision(x + 20, y, width - 20, height)) {
          isVisible = false;
        }
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
