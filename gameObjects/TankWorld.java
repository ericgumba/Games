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
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

/** a
 * Created by ericgumba and Leo Wang on 4/20/17. da a
 */
public class TankWorld extends JPanel implements Runnable{

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
  Thread thread;
  /**
   * Initializes various objects within the game, such as the tanks,
   * bullets, image generators, and wall generators.
   */


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

  public void init() {

    thread = new Thread(this);
    thread.setPriority(Thread.MIN_PRIORITY);
    thread.start();

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
      if (bullet.move()){
        tankOneBullets.remove(bullet);
      } else {
        bullet.draw(gameGraphics, this);
      }
    }

    for ( Bullet bullet : tankTwoBullets ) {
      if(bullet.move()){
        tankOneBullets.remove(bullet);
      } else {
        bullet.draw(gameGraphics, this);
      }
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

    int[][] multi = new int[][]{
        //                                                         h
        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ,1 ,1, 1, 1 ,1, 1, 1, 1 }, //1
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //2
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //3
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //4
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //5
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //6
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //7
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //8
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 }, //9
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //10
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //11
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //12
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //13
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //14
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //15
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //16
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //17
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //18
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //19
        { 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //20
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //21
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //22
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, //23
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //24
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //25
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //26
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //27
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //28
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //29
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //30
        { 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //31
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //32
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //33
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //34
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //35
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //36
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //37
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //38
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //39
        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, //40
    };

    // the moment of truth
    for ( int i = 0; i<levelSize; i++){
      for ( int j = 0; j<levelSize; j++){
        if ( multi[i][j] == 1 ){
          wallGenerator.addWall(j*(BACKGROUND_WIDTH/40), i*(BACKGROUND_HEIGHT/40),'1');
        } else if ( multi[i][j] == 2 ){
          wallGenerator.addWall(j*(BACKGROUND_WIDTH/40), i*(BACKGROUND_HEIGHT/40), '2');
        }
      }
    }
//    try {
//      source = new BufferedReader( new FileReader("gameObjects/Resources/level.txt"));
//    } catch (FileNotFoundException ex) {
//      Logger.getLogger(WallGenerator.class.getName()).log(Level.SEVERE, null, ex);
//    }
//    try {
//      for (int i = 0; i<levelSize; i++) {
//        nextLine = source.readLine();
//        for (int j= 0; j<levelSize; j++) {
//          ch = nextLine.charAt(j);
//          //System.out.println( "Char: " + ch + "("+i+","+j+")"+ j*(borderX/40)+" "+i*(borderY/40));
//          if (ch=='1'){
//            wallGenerator.addWall(j*(BACKGROUND_WIDTH/40),i*(BACKGROUND_HEIGHT/40),ch);
//          } else if (ch=='2') {
//            wallGenerator.addWall(j*(BACKGROUND_WIDTH/40),i*(BACKGROUND_HEIGHT/40),ch);
//          } else if (ch=='3') {
//            startLX=j*(BACKGROUND_WIDTH/40);
//            startLY=i*(BACKGROUND_HEIGHT/40);
//          } else if (ch=='4') {
//            startRX=j*(BACKGROUND_WIDTH/40);
//            startRY=i*(BACKGROUND_HEIGHT/40);
//          } else if (ch=='5') {
//            // not yet defined in assignment.
//          }
//        }
//      }
//    } catch (IOException ex) {
//      Logger.getLogger(WallGenerator.class.getName()).log(Level.SEVERE, null, ex);
//    }
//
//    if( source != null ) {
//      try {
//        source.close();
//      } catch( IOException e ) {}
//    }
  }

}
