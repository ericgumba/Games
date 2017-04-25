package gameObjects;

/**
 * Created by ericgumba and Leo Wang on 4/22/17.
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
public class Bullet extends TankWorld {

  private Image imageOfBullet;
  private int x, y, sizeX, sizeY, xSpeed, ySpeed;


  /**
   * Bullet constructor.
   * @param imageOfBullet
   * @param x
   * @param y
   * @param xSpeed
   * @param ySpeed
   */
  Bullet( Image imageOfBullet, int x, int y, int xSpeed, int ySpeed ) {
    this.imageOfBullet = imageOfBullet;
    this.x = x;
    this.y = y;
    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;
    sizeX = imageOfBullet.getWidth(null);
    sizeY = imageOfBullet.getHeight(null);
  }

  /**
   * If the bullet position <= position of object and position of object >= bullet position then the bullet has collided
   * with another object. The bullet then teleports to the edge of the map.
   * @param x
   * @param y
   * @param w
   * @param h
   * @return
   */

  public boolean collision(int x, int y, int w, int h) {
    if ( y + h > this.y && y < this.y + sizeY
        && x + w > this.x
        && x < this.x+sizeX
        )
    {
      this.x = 0;
      this.y = 0;
      return true;
    }
    return false;
  }

  /**
   * updates position of bullet based off of its velocity.
   */
  public void move() {
    y += ySpeed;
    x += xSpeed;
  }

  public void draw(Graphics g, ImageObserver obs) {
    g.drawImage(imageOfBullet, x, y, obs);
  }
}
