package gameObjects;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/** a
 * Created by ericgumba and leo wang on 4/22/17.
 */
public class PlayerControls extends KeyAdapter {

  private TankWorldEvents tankWorldEvents;

  public PlayerControls( TankWorldEvents tankWorldEvents ) {
    this.tankWorldEvents = tankWorldEvents;
  }

  public void keyPressed( KeyEvent pressed ) {
    tankWorldEvents.setValue(pressed, 1);
  }

  public void keyReleased( KeyEvent released ) {
    tankWorldEvents.setValue( released, 0 );
  }
}
