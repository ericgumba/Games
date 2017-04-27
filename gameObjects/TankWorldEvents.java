package gameObjects;

import java.util.Observable;
import java.awt.event.KeyEvent;

/** a
 * Created by eric gumba and leo wang on 4/22/17. a
 */
public class TankWorldEvents extends Observable {
  public int eventType;
  public Object event;

  public void setValue(KeyEvent k, int keyEventType) {

    eventType = keyEventType;
    event = k;
    setChanged();
      notifyObservers(this);
  }
}
