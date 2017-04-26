package gameObjects;

import static java.applet.Applet.newAudioClip;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.net.URL;

/** a
 * Created by ericgumba and Leo Wang on 4/20/17. da a
 */
public class TankWorld extends JPanel {

  static ImageGenerator imageGenerator;
  static HashMap<Integer, String> controls = new HashMap<>();
  final int BACKGROUND_WIDTH = 1475, BACKGROUND_HEIGHT = 1155;
  private static int playerOneXDisplay, playerOneYDisplay, playerTwoXDisplay, playerTwoYDisplay;
  private final int SCREEN_WIDTH = 840, SCREEN_HEIGHT = 880;
  ImageObserver observer;
  private BufferedImage bufferedImg;
  private Background rockBackground;
  private static Tank tankOne, tankTwo;
  static Tank[] player = new Tank[3];
  private final int levelSize = 40;
  static Image[] explosionFrames;
  static ArrayList<Bullet> tankOneBullets, tankTwoBullets;
  static AudioClip fire, death;
  static WallGenerator wallGenerator;
  int startLX, startLY, startRX, startRY;
  TankWorldEvents tnkWorldEvents;
  PlayerControls gameControls;

  /**
   * Initializes various objects within the game, such as the tanks,
   * bullets, image generators, and wall generators.
   */


  public void init() {

    setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    imageGenerator = new ImageGenerator();
    wallGenerator = new WallGenerator();

    initializePlayerOneAndPlayerTwoControls();

    explosionFrames = new Image[]{
        imageGenerator.getImage("Resources/explosion1.png"),
        imageGenerator.getImage("Resources/explosion2.png"),
        imageGenerator.getImage("Resources/explosion3.png"),
        imageGenerator.getImage("Resources/explosion4.png"),
        imageGenerator.getImage("Resources/explosion5.png"),
        imageGenerator.getImage("Resources/explosion6.png"),
        imageGenerator.getImage("Resources/explosion7.png")
    };

    rockBackground = new Background();

    constructWallPattern();
    tankOneBullets = new ArrayList();
    tankTwoBullets = new ArrayList();

    tankOne = new Tank("Resources/Tank_blue_light_strip60.png", tankTwoBullets, tankOneBullets, imageGenerator.getImage("Resources/enemybullet3.png"), 1);
    tankTwo = new Tank("Resources/Tank_blue_light_strip60.png", tankOneBullets, tankTwoBullets, imageGenerator.getImage("Resources/enemybullet3.png"), 2);

    player[1] = tankTwo;
    player[2] = tankOne;

    this.setFocusable(true);
    observer = this;
    tnkWorldEvents = new TankWorldEvents();


    tnkWorldEvents.addObserver(tankOne);
    tnkWorldEvents.addObserver(tankTwo);

    gameControls = new PlayerControls(tnkWorldEvents);
    addKeyListener( gameControls );
    death = newAudioClip(TankWorld.class.getResource("Resources/Explosion_Large.wav"));
    fire = newAudioClip(TankWorld.class.getResource("Resources/Explosion_small.wav"));
  }


  /**
   * main paint method that controls where the x and y positions of the tanks bullets
   * and walls are. It will also update the display and draw the minimap of the game.
   */
  public void paint( Graphics g ) {
    Dimension d = getSize();

    updateAndDisplay();
    updatePlayerOneDisplay();
    updatePlayerTwoDisplay();


    BufferedImage bufferedImg2 = ( BufferedImage ) createImage( BACKGROUND_WIDTH, BACKGROUND_HEIGHT );
    Graphics2D g3 = bufferedImg2.createGraphics();
    g3.setBackground( getBackground() );
    g3.setRenderingHint( RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY );

    // draws player 1's screen.
    g3.drawImage(bufferedImg.getSubimage( playerOneXDisplay, playerOneYDisplay, SCREEN_WIDTH / 2, SCREEN_HEIGHT ), 0, 0, this );

    // draw player 2's screen.
    g3.drawImage(bufferedImg.getSubimage( playerTwoXDisplay, playerTwoYDisplay, SCREEN_WIDTH / 2, SCREEN_HEIGHT), SCREEN_WIDTH / 2, 0, this  );

    // draw mini map.
    g3.drawImage( bufferedImg.getScaledInstance( d.width / 5, d.height / 5, 1 ), d.width / 2 - ( d.width / 5 ) / 2, d.height * 3 / 4, this );

    // draw the mini map dividing the two players.
    g3.drawLine( d.width / 2 + 2, 0, d.width / 2 + 2, d.height );

    g3.dispose();
    g.drawImage( bufferedImg2, 0, 0, this ); // x = 0, y = 0 means the image is at the top left.


  }

  /**
   * Draws the tanks, bullets and walls of game, and updates them every time they move
   * or get destroyed by bullets.
   */
  private void updateAndDisplay( ) {
    bufferedImg = ( BufferedImage ) createImage( BACKGROUND_WIDTH, BACKGROUND_HEIGHT ); // create image that is x by y
    Graphics2D gameGraphics = bufferedImg.createGraphics();
    gameGraphics.setBackground( getBackground() );
    gameGraphics.setRenderingHint( RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY );

    rockBackground.draw( gameGraphics, this );


    tankOne.move();
    tankOne.draw( gameGraphics, this );
    tankTwo.move();
    tankTwo.draw( gameGraphics, this );

    wallGenerator.draw( gameGraphics );

    for (Bullet bullet : tankOneBullets ) {
      bullet.draw( gameGraphics, this );
      bullet.move();
    }

    for ( Bullet bullet : tankTwoBullets ) {
      bullet.draw(gameGraphics, this);
      bullet.move();

    }
  }

  /**
   * Updates player one's display based off of the position of player one's tank.
   */
  private void updatePlayerOneDisplay(){

    playerOneXDisplay = tankOne.x + 30 - SCREEN_WIDTH / 4;


    if (playerOneXDisplay < 0) {
      playerOneXDisplay = 0;
    } else if ( playerOneXDisplay + SCREEN_WIDTH / 2 > BACKGROUND_WIDTH ) {
      playerOneXDisplay = BACKGROUND_WIDTH - SCREEN_WIDTH / 2;
    }
    playerOneYDisplay = tankOne.y + 30 - SCREEN_HEIGHT / 2;
    if ( playerOneYDisplay < 0 ) {
      playerOneYDisplay = 0;
    } else if ( playerOneYDisplay + SCREEN_HEIGHT > BACKGROUND_HEIGHT ) {
      playerOneYDisplay = BACKGROUND_HEIGHT - SCREEN_HEIGHT;
    }
  }

  /**
   * Updates player two's display based off of the position of player two's tank.
   */
  private void updatePlayerTwoDisplay(){
    playerTwoXDisplay = tankTwo.x + 30 - SCREEN_WIDTH / 4;
    if ( playerTwoXDisplay < 0 ) {
      playerTwoXDisplay = 0;
    } else if ( playerTwoXDisplay + SCREEN_WIDTH / 2 > BACKGROUND_WIDTH ) {
      playerTwoXDisplay = BACKGROUND_WIDTH - SCREEN_WIDTH / 2;
    }
    playerTwoYDisplay = tankTwo.y + 30 - SCREEN_HEIGHT / 2;
    if ( playerTwoYDisplay < 0 ) {
      playerTwoYDisplay = 0;
    } else if ( playerTwoYDisplay + SCREEN_HEIGHT > BACKGROUND_HEIGHT ) {
      playerTwoYDisplay = BACKGROUND_HEIGHT - SCREEN_HEIGHT;
    }
  }

  private void initializePlayerOneAndPlayerTwoControls(){
    controls.put( KeyEvent.VK_LEFT, "left2" );
    controls.put( KeyEvent.VK_UP, "up2" );
    controls.put( KeyEvent.VK_DOWN, "down2" );
    controls.put( KeyEvent.VK_RIGHT, "right2" );
    controls.put( KeyEvent.VK_ENTER, "shoot2" );
    controls.put( KeyEvent.VK_A, "left1" );
    controls.put( KeyEvent.VK_W, "up1"  );
    controls.put( KeyEvent.VK_S, "down1" );
    controls.put( KeyEvent.VK_D, "right1" );
    controls.put( KeyEvent.VK_SPACE, "shoot1" );
  }

  @SuppressWarnings("null")
  public void constructWallPattern() {
    BufferedReader source = null;
    char ch;
    String nextLine;



    try {
      source = new BufferedReader( new FileReader("gameObjects/Resources/level.txt"));
    } catch (FileNotFoundException ex) {
      Logger.getLogger(WallGenerator.class.getName()).log(Level.SEVERE, null, ex);
    }
    try {
      for (int i = 0; i<levelSize; i++) {
        nextLine = source.readLine();
        for (int j= 0; j<levelSize; j++) {
          ch = nextLine.charAt(j);
          //System.out.println( "Char: " + ch + "("+i+","+j+")"+ j*(borderX/40)+" "+i*(borderY/40));
          if (ch=='1'){
            wallGenerator.addWall(j*(BACKGROUND_WIDTH/40) + 12,i*(BACKGROUND_HEIGHT/40),ch);
          } else if (ch=='2') {
            wallGenerator.addWall(j*(BACKGROUND_WIDTH/40) + 12,i*(BACKGROUND_HEIGHT/40),ch);
          } else if (ch=='3') {
            startLX=j*(BACKGROUND_WIDTH/40);
            startLY=i*(BACKGROUND_HEIGHT/40);
          } else if (ch=='4') {
            startRX=j*(BACKGROUND_WIDTH/40);
            startRY=i*(BACKGROUND_HEIGHT/40);
          } else if (ch=='5') {
            // not yet defined in assignment.
          }
        }
      }
    } catch (IOException ex) {
      Logger.getLogger(WallGenerator.class.getName()).log(Level.SEVERE, null, ex);
    }

    if( source != null ) {
      try {
        source.close();
      } catch( IOException e ) {}
    }
  }

}
