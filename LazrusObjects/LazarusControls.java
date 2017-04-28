package LazrusObjects;

import gameObjects.TankWorldEvents;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by ericgumba on 4/27/17.
 */
public class LazarusControls extends KeyAdapter {

  private LazarusEvents lazEvents;

  public LazarusControls( LazarusEvents lazEvents ) {
    this.lazEvents = lazEvents;
  }

  public void keyPressed( KeyEvent pressed ) { lazEvents.setValue(pressed, 1); }

  public void keyReleased( KeyEvent released ) {
    lazEvents.setValue(released, 0);
  }

}
