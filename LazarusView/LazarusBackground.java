package LazarusView;

import java.awt.*;
import java.awt.image.ImageObserver;
/**
 * Created by ericgumba on 4/26/17.
 */
public class LazarusBackground extends LazarusWorld {
  private Image gameBoard = imgGen.getImage("Lazarus/Background.png");

  public void draw(Graphics2D g, ImageObserver obs){
    g.drawImage(gameBoard, 0, 0, obs);
  }
}
