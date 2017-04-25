package gameObjects;

import java.awt.*;

/**
 * Created by ericgumba and Leo Wang on 4/23/17.
 */
public class IndestructibleWall extends TankWorld {

  private Image wallImage = imageGenerator.getImage("Images/Blue_wall1.png");
  private int x, y, width, height;

  IndestructibleWall(int x, int y) {
    this.x = x;
    this.y = y;
    width = wallImage.getWidth(null);
    height = wallImage.getHeight(null);
  }

  public void update() {
    for ( Bullet bullet : tankOneBullets )
      if ( bullet.collision( x + 20, y, width - 20, height )){
        // add sound generation later.
      }
    for ( Bullet bullet : tankTwoBullets ) {
      if ( bullet.collision( x + 20, y, width - 20, height )) {
        // add sound generation later.
      }
    }
  }

  public void draw( Graphics g ) {
      g.drawImage( wallImage, x, y, observer );
  }

  public boolean collision( int oX, int oY, int oW, int oH ) {
      if (( oY + oH > this.y ) && ( oY < this.y + height)) {
        if (( oX + oW > this.x ) && ( oX < this.x + width )) {
          return true;
        }
      }
    return false;
  }
}
