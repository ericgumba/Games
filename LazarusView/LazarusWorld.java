package LazarusView;

import LazarusModel.Lazarus;
import LazarusModel.WoodCrate;
import gameObjects.Background;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * Created by ericgumba on 4/26/17.
 */
public class LazarusWorld extends JPanel {
  private BufferedImage bufferedImg;
  static ImageGenerator imgGen;
  LazarusBackground lazBackground;
  ImageObserver observer;
  final int GAMEBOARD_WIDTH = 640, GAMEBOARD_HEIGHT = 480;


  public void init(){
    imgGen = new ImageGenerator();

    WoodCrate woodCrate = new WoodCrate(imgGen.getImage("Lazarus/CardBox.png"),0,0);

    lazBackground = new LazarusBackground();
    this.setFocusable(true);


    observer = this;

  }
  public void paint(Graphics g){

    Dimension d = getSize();


    updateAndDisplay();

    BufferedImage bufferedImg2 = ( BufferedImage ) createImage( GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT );
    Graphics2D g3 = bufferedImg2.createGraphics();
    g3.setBackground( getBackground() );
    g3.setRenderingHint( RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY );

    g3.dispose();
    g.drawImage( imgGen.getImage("Lazarus/Background.png"), 0, 0, this ); // x = 0, y = 0 means the image is at the top left.


  }
  public void updateAndDisplay(){

    bufferedImg = ( BufferedImage ) createImage( GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT ); // create image that is x by y
    Graphics2D gameGraphics = bufferedImg.createGraphics();
    gameGraphics.setBackground( getBackground() );
    gameGraphics.setRenderingHint( RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY );
  }

}
