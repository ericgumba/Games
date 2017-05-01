package gameObjects;

import java.awt.*;
import java.awt.image.ImageObserver;
/** a
 * Created by Eric Gumba and Leo Wang on 4/14/17. .
 */
public class Background extends TankWorld {

  private Image gameBoard = imageGenerator.getImage("Resources/BackgroundFullBig2.png");

  public void draw(Graphics2D graphics, ImageObserver observer) {
    graphics.drawImage(gameBoard, 0, 0, observer);
  }
}