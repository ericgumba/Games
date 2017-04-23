package gameObjects;

import java.util.Observable;
import java.awt.event.KeyEvent;

/**
 * Created by ericgumba on 4/22/17.
 */
public class TankWorldEvents extends Observable {
  public Object tankEvent;
  public int keyEventType;

  public void setTankEvent(KeyEvent keyEvent, int eventType){
    keyEventType = eventType;
    tankEvent = keyEvent;
    setChanged();


    try{
      notifyObservers( this );
    }
    catch (Exception error) {
      System.out.println("Error found in TankWorldEvents.java: Invalid key pressed");
    }
  }
}
