package gameObjects;

/**
 * Created by ericgumba on 4/22/17.
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
public class Bullet extends TankWorld {
  Image imageOfBullet;
  int xLocationOfBullet, yLocationOfBullet, widthOfBullet, heightOfBullet, xVelocityOfBullet, yVelocityOfBullet;

  Bullet(Image imageOfBullet, int xSpawnPoint, int ySpawnPoint, int xVelocity, int yVelocity){

    this.imageOfBullet = imageOfBullet;
    xLocationOfBullet = xSpawnPoint;
    yLocationOfBullet = ySpawnPoint;
    xVelocityOfBullet = xVelocity;
    yVelocityOfBullet = yVelocity;

    widthOfBullet = imageOfBullet.getWidth(null);
    heightOfBullet = imageOfBullet.getHeight(null);
  }

  public boolean hasCollided(int xLocationOfObject, int yLocationOfObject, int widthOfObject, int heightOfObject) {

    if(yLocationOfObject + heightOfObject > yLocationOfBullet
        && yLocationOfObject < yLocationOfBullet + heightOfBullet
        && xLocationOfObject + widthOfObject > xLocationOfBullet
        && xLocationOfObject < xLocationOfBullet + widthOfBullet) {

      xLocationOfBullet = 0;
      yLocationOfBullet = 0;
      return true;
    } else {
      return false;
    }
  }
  public void moveBullet() {
    yLocationOfBullet += yVelocityOfBullet;
    xLocationOfBullet += xVelocityOfBullet;
  }

  public void drawBullet(Graphics graphics, ImageObserver imageObserver){
    graphics.drawImage(imageOfBullet, xVelocityOfBullet, yVelocityOfBullet, imageObserver);
  }
}
