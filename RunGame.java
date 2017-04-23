import gameObjects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

public class RunGame {
  public static void main(String[] args) {

    /* Correct

     */
    int screenWidth = 1280;
    int screenHeight = 1280;
    final TankWorld game = new TankWorld();
    game.init();
    JFrame f = new JFrame("TankWarz");
    f.addWindowListener(new WindowAdapter() {
    });
    f.getContentPane().add("Center", game);
    f.pack();
    f.setSize(new Dimension(screenWidth, screenHeight));
    f.setVisible(true);
    f.setResizable(true);
    game.start();
//
  }

}
