package gameObjects;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/** a
 * Created by eric gumba and leo wang on 4/22/17.
 */
public class PlayerControls extends KeyAdapter {

  private TankWorldEvents tankWorldEvents;
  private NetworkEvents networkEvents;
  private HashMap<Integer, String> directsOrControls;
  private int gameMode;

  
  /** 
   * PlayerControls constructor
   * @param tankWorldEvents
   * @param networkEvents
   * @param directsOrControls
   * @param gameMode
   */ 
  
  public PlayerControls( TankWorldEvents tankWorldEvents,
                        NetworkEvents networkEvents, 
                        HashMap<Integer, String> directsOrControls, 
                        int gameMode ) {
    this.tankWorldEvents = tankWorldEvents;
    this.networkEvents = networkEvents;
    this.directsOrControls = directsOrControls;
    this.gameMode = gameMode;
  }

  /** 
   * keyPressed method 
   * @param pressed
   */   
  public void keyPressed( KeyEvent pressed ) {
        String action = directsOrControls.get(pressed.getKeyCode());
        if(null==action) { // filter out unnecessary changes on observable
        } else {
          if ((gameMode==1)||(gameMode==2)){
              networkEvents.setValue(pressed.getKeyCode(), 1);
          }
          tankWorldEvents.setValue(pressed.getKeyCode(), 1);
          //System.out.println("keyboard entry "+pressed.getKeyCode()+" "+action+" 1");
        }            
  }

  /** 
   * keyReleased method 
   * @param released
   */  
  public void keyReleased( KeyEvent released ) {
        String action = directsOrControls.get(released.getKeyCode());
        if(null==action) {  // filter out unnecessary changes on observable
        } else {
          if ((gameMode==1)||(gameMode==2)){
              networkEvents.setValue(released.getKeyCode(), 0);
          }
          tankWorldEvents.setValue(released.getKeyCode(), 0);
          //System.out.println("keyboard entry "+released.getKeyCode()+" "+action+" 0");
        }            
  }
}
