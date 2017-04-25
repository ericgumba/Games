package gameObjects;

import java.util.Observable;
import java.awt.event.KeyEvent;

/**
 * Created by ericgumba on 4/22/17.
 */
public class TankWorldEvents extends Observable {
  public int eventType;
  public Object event;

  public void setValue(KeyEvent k, int keyEventType) {

    eventType = keyEventType;
    event = k;
    setChanged();
    try {
      notifyObservers(this);
    } catch (Exception e){
      System.out.println("Wrong button was pressed.");
    }
  }
}
