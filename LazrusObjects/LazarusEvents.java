package LazrusObjects;

import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 * Created by ericgumba on 4/27/17. GOOD
 */
public class LazarusEvents extends Observable {
  public int eventType;
  public Object event;

  public void setValue(KeyEvent k, int keyEventType) {

    eventType = keyEventType;
    event = k;
    setChanged();
    notifyObservers(this);
  }
}
