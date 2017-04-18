import gameObjects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

public class RunGame {
  public static void main(String[] args) {
    int screenWidth = 1280;
    int screenHeight = 1280;
    final TankWorld game = new TankWorld();
    game.initializeGame();
    JFrame f = new JFrame("TankWarz");
    f.addWindowListener(new WindowAdapter() {
    });
    f.getContentPane().add("Center", preAlpha);
    f.pack();
    f.setSize(new Dimension(screenWidth, screenHeight));
    f.setVisible(true);
    f.setResizable(false);
    game.start();

  }

}
