package LazrusObjects;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by ericgumba on 4/27/17.
 */
public interface MainCharacterInterface extends Observer {
  void draw(Graphics g, ImageObserver obs);
  void move();
}
