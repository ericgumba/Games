package game;

import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 * @ SFSU Spring 2017 CSC413 Assignment-4 4/18/2017
 */

public class GameEvents extends Observable {

    public int eventType; // 0: key released, 1 = key pressed,  2 = in-game events, 3 = timed events
    public Object event;  //this is made an object so it can be int, string etc

    // user keyboard input event type
    public void setValue(KeyEvent k, int keyEventType) {
        eventType = keyEventType;
        event = k;
        setChanged();
        notifyObservers(this);
    }

    // in-game event type
    public void setValue(String msg) {
        eventType = 2;
        event = msg;
        setChanged();
        notifyObservers(this);
    }

    // timed event type
    public void setValue(int timedEvent) {
        eventType = 3;
        event = timedEvent;
        setChanged();
        notifyObservers(this);
    }
}
