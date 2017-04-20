package gameObjects;

import java.awt.*;
import java.awt.image.ImageObserver;
/**
 * Created by ericgumba on 4/14/17.
 */
public class Background extends TankWorld {

  private Image gameBoard = getImage("Images/Background.png");

  public void draw(Graphics2D graphics, ImageObserver observer) {
    graphics.drawImage(gameBoard, 0, 0, observer);

  }
}//