package GameInterface;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

/** a
 * Created by ericgumba on 4/25/17.
 */
public interface VehicleInterface extends Observer {
  void draw(Graphics g, ImageObserver obs);
  void move();
}
