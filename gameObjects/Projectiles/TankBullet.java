package gameObjects.Projectiles;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;


/**
 * Created by ericgumba on 4/18/17.
 */
public abstract class TankBullet {
  Image bullet;
  int x, y, xVelocity, yVelocity, xSize, ySize;
  ImageObserver observer;


  public abstract int damage();

  public boolean collide(int x, int y, int w, int h){

  }
}