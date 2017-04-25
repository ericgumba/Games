package gameObjects;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Created by ericgumba on 4/23/17.
 */
public class DestructibleWall extends TankWorld {
  private Image wallImage = imageGenerator.getImage("Images/Blue_wall2.png");
  private int xSpawnPoint, ySpawnPoint, wallWidth, wallHeight;
  private int hp = 1;


  DestructibleWall(int xSpawnPoint, int y) {
    this.xSpawnPoint = xSpawnPoint;
    this.ySpawnPoint = y;
    wallWidth = wallImage.getWidth(null);
    wallHeight = wallImage.getHeight(null);
  }

  public void update() {
    if ( hp == 1 ) // isVisible
    {
      for (Bullet bullet : tankOneBullets){
        if (bullet.collision(xSpawnPoint + 20, ySpawnPoint, wallWidth - 20, wallHeight)) {
          hp -= 1;
        }
      }
      for ( Bullet bullet : tankTwoBullets ){
        if ( bullet.collision( xSpawnPoint + 20, ySpawnPoint, wallWidth - 20, wallHeight )) {
          hp -= 1;
        }
      }
    }
  }

  public void draw( Graphics g ) {
    if ( hp == 1 )
    {
      g.drawImage( wallImage, xSpawnPoint, ySpawnPoint, observer );
    }
  }

  public boolean collision( int xPositionOfObject, int yPositionOfObject, int widthOfObject, int heightOfObject ) {
    if ( hp == 1 )
    {
      if ((yPositionOfObject + heightOfObject > this.ySpawnPoint) && (yPositionOfObject < this.ySpawnPoint + wallHeight)) {
        if ((xPositionOfObject + widthOfObject > this.xSpawnPoint) && (xPositionOfObject < this.xSpawnPoint + wallWidth)) {
          return true;
        }
      }
      return false;
    }
    return false;
  }
}
