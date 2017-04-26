package LazarusView;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Created by ericgumba on 4/26/17.
 */
public class LazarusWorld extends JPanel {
  static ImageGenerator imgGen;
  LazarusBackground lazBackground;
  ImageObserver observer;
  final int GAMEBOARD_WIDTH = 640, GAMEBOARD_HEIGHT = 480;

  public void init(){
    imgGen = new ImageGenerator();

    lazBackground = new LazarusBackground();

    this.setFocusable(true);



  }
  public void paint(Graphics g){

  }

}
