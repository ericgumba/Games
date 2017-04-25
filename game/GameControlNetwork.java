package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @ SFSU Spring 2017 CSC413 assignment-4 4/18/2017
 */

public class GameControlNetwork extends KeyAdapter {

    GameEvents gE;

    public GameControlNetwork(GameEvents gE) {
        this.gE = gE;
    }

    /*
    public GameControlNetwork(GameEvents gE) {
        this.gE = gE;
    }
    */
    
    public void keyPressed(KeyEvent p) {
        
        gE.setValue(p, 1);
        
    }
    
    public void keyReleased(KeyEvent r) {
        gE.setValue(r, 0);
    }
}
