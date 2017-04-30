package LazrusObjects;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.*;

/**
 * Created by ericgumba on 4/26/17.
 */
public class LazarusWorld extends JPanel implements Runnable {


  static java.util.List<Stack<Box>> boxWeights = new ArrayList<Stack<Box>>();
  private BufferedImage bufferedImg, bufferedImg2;
  static ImageGenerator imgGen;
  LazarusBackground lazBackground;
  public ImageObserver observer;
  final int GAMEBOARD_WIDTH = 640, GAMEBOARD_HEIGHT = 480;
  LazarusEvents lazEvents;
  LazarusControls lazControls;
  static MainCharacter mc;
  static HashMap<Integer, String> controls = new HashMap<>();
  Thread thread;
  final int BOX_SPAWN_TIMER = 100;
  static BoxGenerator boxGen;
  static ArrayList<Box> lazBoxes;
  static HashMap<Integer, Integer> boxPositions = new HashMap<>();


  @Override
  public void run() {
    Thread me = Thread.currentThread();
    while (thread == me) {
      repaint();
      try {
        thread.sleep(20);
      } catch (InterruptedException e) {
        break;
      }
    }
  }
  public void init(){

    thread = new Thread(this);
    thread.setPriority(Thread.MIN_PRIORITY);
    thread.start();


    for(int i = 0; i < 16; i++) {
      boxPositions.put(i*40, i);
    }


    System.out.println(boxPositions);
    for ( int i = 0; i < 16; i++){
      boxWeights.add( new Stack< Box >());
    }

    imgGen = new ImageGenerator();
    boxGen = new BoxGenerator( );

    mc = new MainCharacter();
    controls.put( KeyEvent.VK_LEFT, "left" );
    controls.put( KeyEvent.VK_RIGHT, "right" );
    controls.put( KeyEvent.VK_SPACE, "space" );

    lazBackground = new LazarusBackground();
    this.setFocusable( true );
    observer = this;


    lazEvents = new LazarusEvents();
    lazEvents.addObserver( mc );
    lazControls = new LazarusControls( lazEvents );
    addKeyListener( lazControls );

  }
  public void paint( Graphics g ){

    Dimension d = getSize();
    updateAndDisplay();
    bufferedImg2 = ( BufferedImage ) createImage( GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT );
    Graphics2D g3 = bufferedImg2.createGraphics();
    g3.setBackground( getBackground() );
    g3.setRenderingHint( RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY );
    g3.clearRect(0,0, GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT);
    g3.drawImage( bufferedImg.getSubimage(0, 0, GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT),0,0,this);
    g3.dispose();
    g.drawImage( bufferedImg2,0,0,this);

  }
  public void updateAndDisplay(){
    bufferedImg = ( BufferedImage ) createImage( GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT ); // create image that is x by y
    Graphics2D gameGraphics = bufferedImg.createGraphics();
    gameGraphics.setBackground( getBackground() );
    gameGraphics.setRenderingHint( RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY );
    gameGraphics.clearRect(0,0,GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT);

    lazBackground.draw(gameGraphics, this);

    mc.draw(gameGraphics, this);
    mc.move();
    boxGen.draw(gameGraphics, this);


  }

}
