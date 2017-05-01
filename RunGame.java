import gameObjects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

/*
*  java RunGame <enter>                 - single machine mode
*  java RunGame 9191 <enter>            - Two machine server mode (player 1)
*  java RunGame IPaddress 9191 <enter>  - Two machine client mode (player 2)
*  
*  Two machine network play must start the server first, then client 
*/

public class RunGame {
  public static void main(String[] args) {
      
    /* Correct
     */
    int screenWidth = 1280;
    int screenHeight = 800;
    final TankWorld game = new TankWorld();
    game.initNet(args);
    game.init();
    JFrame f = new JFrame("TankWarz");
    f.addWindowListener(new WindowAdapter() {
    });
    f.getContentPane().add("Center", game);
    f.pack();
    f.setSize(new Dimension(screenWidth, screenHeight));
    f.setVisible(true);
    f.setResizable(true);
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

  }

}