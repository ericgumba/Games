package GameInterface;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;
/**
 * Created by ericgumba on 4/22/17.
 */
public interface VehicleInterface extends Observer {
  void update(Observable object, Object argument);
  void draw(Graphics graphics, ImageObserver observer);
  void move();
}
