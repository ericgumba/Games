package gameObjects;

import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import static java.applet.Applet.newAudioClip;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

/** a
 * Created by eric gumba and Leo Wang on 4/20/17.
 */
public class TankWorld extends JPanel implements Runnable{

  // New for network play  
  static int gameMode;  // 0 : single machine mode, 1: server mode, 2: client mode
  static Socket socketConnection;
  NetworkWriter networkWriter;
  NetworkReader networkReader;
  NetworkEvents networkEvents;
  static HashMap<Integer, String> directs = new HashMap<>(); //network direct control
  
  // original single machine play 
  static ImageGenerator imageGenerator;
  static HashMap<Integer, String> controls = new HashMap<>();
  final int BACKGROUND_WIDTH = 1280, BACKGROUND_HEIGHT = 1280; 
  private static int playerOneXDisplay, playerOneYDisplay, playerTwoXDisplay, playerTwoYDisplay;
  private final int SCREEN_WIDTH = 1280, SCREEN_HEIGHT = 800;
  Font scoreFont = new Font("Impact", Font.PLAIN, 50);
  ImageObserver observer;
  private BufferedImage bufferedImg;
  private Background rockBackground;
  //private static Tank tankOne, tankTwo;
  static Tank tankOne, tankTwo;
  static Tank[] player = new Tank[3];
  private final int levelSize = 40;
  static Image[] explosionFrames;
  static ArrayList<Bullet> tankOneBullets, tankTwoBullets;
  static AudioClip fire, death;
  static WallGenerator wallGenerator;
  //int startLX, startLY, startRX, startRY;
  TankWorldEvents tnkWorldEvents;
  PlayerControls gameControls;
  Thread thread;
  /**
   * Initializes various objects within the game, such as the tanks,
   * bullets, image generators, and wall generators.
   */

  /**
   * run method 
   * TankWorld extends JPanel implements Runnable
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

 /**
   * initNet method 
   * initialization for network play
   * @param args
   *  java RunGame                 - single machine mode
   *  java RunGame 9191            - Two machine server mode (player 1)
   *  java RunGame IPaddress 9191  - Two machine client mode (player 2)
   */    
  public void initNet(String[] args) {
    
    Integer argumentLength = args.length;

   if (argumentLength == 1) {
        String number = args[0];
        Integer port = Integer.valueOf(number);
        System.out.println("Game is running in Server mode, port ="+port);
        gameMode = 1; 
            try {
                ServerSocket server = new ServerSocket( port );
                socketConnection = server.accept();
                } catch( IOException exception ) {
                    System.err.println( "Failed to establish connection" );
                }
    } else if (argumentLength == 2) {
        String ipAddress = args[0];
        String number = args[1];
        Integer port = Integer.valueOf(number);
        System.out.println("Game is running in Client mode: IPaddress "+ipAddress+" port "+port);
        gameMode= 2;
        try {
                socketConnection = new Socket(ipAddress, port);
            } catch( IOException exception ) {
                    System.err.println( "Failed to establish connection" );
            }  
    } else {
        System.out.println("Game is running in Single machine mode");
        gameMode= 0;
    }    
   
   
  }

 /**
   * init method 
   * initialization of the game
   * @param args
   */     
  public void init() {

    thread = new Thread(this);
    thread.setPriority(Thread.MIN_PRIORITY);
    thread.start();

    imageGenerator = new ImageGenerator();
    wallGenerator = new WallGenerator();

    initializePlayerOneAndPlayerTwoControls();
    if (gameMode == 1) {
        initializeGameModeOneControls();        
    } else if (gameMode == 2) {
        initializeGameModeTwoControls();       
    }   
    
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

    player[1] = tankTwo;  // player1 (tankOne)'s enemy player is tankTwo
    player[2] = tankOne;  // player2 (tankTwo)'s enemy player is tankOne

    this.setFocusable(true);
    observer = this;
    tnkWorldEvents = new TankWorldEvents();

    tnkWorldEvents.addObserver(tankOne);
    tnkWorldEvents.addObserver(tankTwo);
    
    // network play setup
    if (gameMode ==1 || gameMode==2) {  // network play
        networkEvents = new NetworkEvents();
        networkWriter = new NetworkWriter(socketConnection);  
        networkEvents.addObserver(networkWriter);  
        networkReader = new NetworkReader(socketConnection, tnkWorldEvents);   
        thread = new Thread(networkReader);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
        gameControls = new PlayerControls(tnkWorldEvents, networkEvents, directs, gameMode);
    } else {   // single machine play
        gameControls = new PlayerControls(tnkWorldEvents, networkEvents, controls, gameMode);
    }

    addKeyListener( gameControls );

    death = newAudioClip(TankWorld.class.getResource("Resources/Explosion_Large.wav"));
    fire = newAudioClip(TankWorld.class.getResource("Resources/Explosion_small.wav"));
  }
  
  /**
   *  paint method 
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

    // draw score on lower corners
    g3.setFont(scoreFont);
    g3.setColor(Color.BLUE);
    g3.drawString(tankOne.getScore() + "", 60, d.height-30);
    g3.setColor(Color.RED);
    g3.drawString(tankTwo.getScore() + "", d.width -100, d.height-30);
    
    // draw the line dividing the two players's map.
    g3.drawLine( d.width / 2 + 2, 0, d.width / 2 + 2, d.height );
    
    // draw mini map.
    g3.drawRect (d.width / 2 - (d.width / 5) / 2 - 1, d.height * 3 / 5 - 1, d.width / 5 + 1, d.width / 5 + 1);
    g3.drawImage(bufferedImg.getScaledInstance(d.width / 5, d.width / 5, 1), d.width / 2 - (d.width / 5) / 2, d.height * 3 / 5, this);
    
    g3.dispose();
    g.drawImage( bufferedImg2, 0, 0, this ); // x = 0, y = 0 means the image is at the top left.

  }

  /**
   *  updateAndDisplay method 
   * Draws the tanks, bullets and walls of game, and updates them every time they move
   * or get destroyed by bullets.
   */
  private void updateAndDisplay( ) {
    bufferedImg = (BufferedImage) createImage(BACKGROUND_WIDTH, BACKGROUND_HEIGHT); // create image that is x by y
    Graphics2D gameGraphics = bufferedImg.createGraphics();
    gameGraphics.setBackground(getBackground());
    gameGraphics.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);

    if ( tankOne.getScore() != 0 && tankTwo.getScore() != 0 ) {

      rockBackground.draw(gameGraphics, this);

      tankOne.move();
      tankOne.draw(gameGraphics, this);
      tankTwo.move();
      tankTwo.draw(gameGraphics, this);

      wallGenerator.draw(gameGraphics);

      for (int i = 0; i < tankOneBullets.size(); i++) {
        if (tankOneBullets.get(i).move()) {
          tankOneBullets.remove(i);
        } else {
          tankOneBullets.get(i).draw(gameGraphics, this);
        }
      }

      for (int i = 0; i < tankTwoBullets.size(); i++) {
        if (tankTwoBullets.get(i).move()) {
          tankTwoBullets.remove(i);
        } else {
          tankTwoBullets.get(i).draw(gameGraphics, this);
        }
      }
    } else{
      rockBackground.draw(gameGraphics, this);
      //g2.drawImage(getSprite("Resources/gameOver3.png"), (borderX / 2) - 125, (borderY / 4) - 40, observer);
      // Drawing the Score in an aligned box:
      //String gameOverMessage = "Game Over";
      String scoreMessage;
      if ( tankOne.getScore() == 0 ) {
        scoreMessage = "Player Two Wins!";
      } else {
        scoreMessage = "Player One Wins!";
      }
      Font l = new Font("Garamond", Font.BOLD, 48);
      gameGraphics.setFont(l);

      //measure the message 'greeting'
      FontRenderContext context = gameGraphics.getFontRenderContext(); //gets font characteristics specific to screen res.
      Rectangle2D bounds = l.getStringBounds(scoreMessage, context);

      //set (x,y) = top left corner of text rectangle
      double x = (getWidth() - bounds.getWidth()) / 2;
      double y = (getHeight() - bounds.getHeight()) / 2;

      //add ascent to y to reach the baseline
      double ascent = -bounds.getY();
      double baseY = y + ascent;

      gameGraphics.setPaint(Color.GREEN);

      //Now, draw the centered, styled message
      //g2.drawString(gameOverMessage, (borderX / 2) - 160, (borderY / 4) - 40);
      gameGraphics.drawString(scoreMessage, (int) x, (int) baseY);
    }
  }
  
  /**
   *  updatePlayerOneDisplay method
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
   *  updatePlayerTwoDisplay method
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

 /**
   *  initializePlayerOneAndPlayerTwoControls method
   * initialization of HashMap "controls" 
   */
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

 /**
   *  initializePlayerOneControls method
   * initialization of HashMap "directs" for server mode 
   */
  private void initializeGameModeOneControls(){  // server control 
    directs.put( KeyEvent.VK_A, "left1" );
    directs.put( KeyEvent.VK_W, "up1"  );
    directs.put( KeyEvent.VK_S, "down1" );
    directs.put( KeyEvent.VK_D, "right1" );
    directs.put( KeyEvent.VK_SPACE, "shoot1" );
  }

 /**
   *  initializePlayerTwoControls method
   * initialization of HashMap "directs" for client mode
   */   
  private void initializeGameModeTwoControls(){  // client control 
    directs.put( KeyEvent.VK_LEFT, "left2" );
    directs.put( KeyEvent.VK_UP, "up2"  );
    directs.put( KeyEvent.VK_DOWN, "down2" );
    directs.put( KeyEvent.VK_RIGHT, "right2" );
    directs.put( KeyEvent.VK_ENTER, "shoot2" );
  }

 /**
   *  constructWallPattern method
   * initialization of walls in the game map
   */  
  @SuppressWarnings("null")
  public void constructWallPattern() {
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
  }

}
