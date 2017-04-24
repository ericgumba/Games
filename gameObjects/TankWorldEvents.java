package gameObjects;

import java.util.Observable;
import java.awt.event.KeyEvent;

/**
 * Created by ericgumba on 4/22/17.
 */
public class TankWorldEvents extends Observable {
  public int eventType; // 1 = user input,  2 = in-game events, 3 = timed events
  public Object event;  //this is made an object so it can be int, string etc

  public void setValue(KeyEvent k, int keyEventType) {

    eventType = keyEventType;
    event = k;
    setChanged();
    try {
      notifyObservers(this);
    } catch (Exception e){
      System.out.println("I wish I majored in math");
    }
  }
}
