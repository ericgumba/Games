import LazrusObjects.LazarusWorld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
/**
 * Created by ericgumba on 4/26/17.
 */
public class RunLazarus {
  public static void main(String[] args) {


  /* Correct
   */
  int screenWidth = 640;
  int screenHeight = 480;
  final LazarusWorld game = new LazarusWorld();
    game.init();
  JFrame f = new JFrame("Lazarus");
    f.addWindowListener(new

  WindowAdapter() {
  });
    f.getContentPane().

  add("Center",game);
    f.pack();
    f.setSize(new

  Dimension(screenWidth, screenHeight));
    f.setVisible(true);
    f.setResizable(true);
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }
}
