package gameObjects;

import java.util.Observable;
import java.awt.event.KeyEvent;

/** a
 * Created by eric gumba and leo wang on 4/22/17. a
 */
public class NetworkEvents extends Observable {
  public int eventType;
  public int event;

  /** 
   * setValue method
   * @param k
   * @param keyEventType
   */      
  public void setValue(int k, int keyEventType) {

    eventType = keyEventType;
    event = k;
    setChanged();
      notifyObservers(this);
  }
}
