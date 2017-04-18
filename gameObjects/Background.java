package gameObjects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
/**
 * Created by ericgumba on 4/14/17.
 */
public class Background extends TankWorld {

  Image map = getSprite("Images/Background.png");

  public void draw(Graphics2D graphics, ImageObserver observer) {
    graphics.drawImage(map, 0, 0, obs);

  }
}