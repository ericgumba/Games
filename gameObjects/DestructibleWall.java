package gameObjects;

import java.awt.*;

/** a
 * Created by ericgumba and leo wang on 4/23/17. d
 */
public class DestructibleWall extends TankWorld {
  private Image wallImage = imageGenerator.getImage("Resources/Blue_wall2.png");
  private int xLocationOfWall, yLocationOfWall, wallWidth, wallHeight;
  private int hp = 1;


  DestructibleWall(int xSpawnPoint, int ySpawnPoint) {
    this.xLocationOfWall = xSpawnPoint;
    this.yLocationOfWall = ySpawnPoint;
    wallWidth = wallImage.getWidth(null);
    wallHeight = wallImage.getHeight(null);
  }

  public void update() {
    if ( hp == 1 ) // isVisible
    {
      for (Bullet bullet : tankOneBullets){
        if (bullet.collision(xLocationOfWall + 20, yLocationOfWall, wallWidth - 20, wallHeight)) {
          hp -= 1;
          fire.play();
        }
      }
      for ( Bullet bullet : tankTwoBullets ){
        if ( bullet.collision( xLocationOfWall + 20, yLocationOfWall, wallWidth - 20, wallHeight )) {
          hp -= 1;
          fire.play();
        }
      }
    }
  }

  public void draw( Graphics g ) {
    if ( hp == 1 )
    {
      g.drawImage( wallImage, xLocationOfWall, yLocationOfWall, observer );
    }
  }

  public boolean collision( int xPositionOfObject, int yPositionOfObject, int widthOfObject, int heightOfObject ) {
    if ( hp == 1 )
    {
      if (
          yPositionOfObject + heightOfObject > this.yLocationOfWall
          && yPositionOfObject < this.yLocationOfWall + wallHeight
              && xPositionOfObject + widthOfObject > this.xLocationOfWall
              &&  xPositionOfObject < this.xLocationOfWall + wallWidth
          ) {
          return true;
        }

      return false;
    }
    return false;
  }
}
