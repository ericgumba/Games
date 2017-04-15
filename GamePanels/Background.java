package GamePanels;

import javax.swing.*;

/**
 * Created by ericgumba on 4/14/17.
 */
public class Background extends JFrame {
  private final int WIDTH = 700;
  private final int HEIGHT = 700;
  private String board = "Images/Background.png";

  public Background() {
    ImageIcon game = new ImageIcon( board );

    JLabel boardLabel = new JLabel("test", game, JLabel.CENTER);
    add(boardLabel);
    setSize(WIDTH, HEIGHT);
    setTitle(" board example ");
    setVisible( true );

    setResizable( false );


  }
}
