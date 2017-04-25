package gameObjects;

/**
 * Created by ericgumba and Leo Wang on 4/22/17.
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
public class Bullet extends TankWorld {

  private Image imageOfBullet; //the caller, whether enemy or user must provide the bullet
  private int x, y, sizeX, sizeY, xSpeed, ySpeed;



  Bullet( Image imageOfBullet, int x, int y, int xSpeed, int ySpeed ) {
    this.imageOfBullet = imageOfBullet;//recieve different bullet image when power-up is picked up
    this.x = x;
    this.y = y;
    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;
    sizeX = imageOfBullet.getWidth(null);
    sizeY = imageOfBullet.getHeight(null);
  }

  public boolean collision(int x, int y, int w, int h) {
    if ( y + h > this.y && y < this.y + sizeY
        && x + w > this.x
        && x < this.x+sizeX
        )
    {
      this.x = 0;
      this.y = 0; // When it reaches here. it must be removed.
      return true;
    }
    return false;
  }

  public void move() {
    y += ySpeed;
    x += xSpeed;
  }

  public void draw(Graphics g, ImageObserver obs) {
    g.drawImage(imageOfBullet, x, y, obs);
  }
}
