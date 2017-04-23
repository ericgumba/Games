package gameObjects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.applet.AudioClip;
import java.util.ArrayList;
/**
 * Created by ericgumba on 4/20/17.
 */
public class TankWorld extends JApplet {

  final int GAME_BOARD_WIDTH = 1500, GAME_BOARD_HEIGHT = 1200;
  final static int APPLET_WIDTH = 840, APPLET_HEIGHT = 880;
  static ImageGenerator imageGenerator;
  static HashMap<Integer, String> playerOneAndPlayerTwoControls;
  Background gameBoard; // It's necessary to initialize this in the initializeGame method.
  ImageObserver observer;
  BufferedImage bufferedImage, bufferedImage2;
  Graphics2D graphics2D;
  static Tank playerOneTank, playerTwoTank;
  static Tank[] player = new Tank[3];
  static Image[] explosionFrames;
  static ArrayList<Bullet> playerOneBullets, playerTwoBullets;
  static AudioClip fire, death;
  TankWorldEvents tankWorldEvents;
  PlayerControls playerControls;
  static int playerOneXDisplay, playerOneYDisplay, playerTwoXDisplay, playerTwoYDisplay;

  public void init(){

    setSize(APPLET_WIDTH, APPLET_HEIGHT);
    playerOneAndPlayerTwoControls = new HashMap<>();
    imageGenerator = new ImageGenerator();
    gameBoard = new Background();

    this.setFocusable(true);
    observer = this;

    // initialize player one and player two controls.
    playerOneAndPlayerTwoControls.put(KeyEvent.VK_LEFT, "left2");
    playerOneAndPlayerTwoControls.put(KeyEvent.VK_RIGHT, "right2");
    playerOneAndPlayerTwoControls.put(KeyEvent.VK_DOWN, "down2");
    playerOneAndPlayerTwoControls.put(KeyEvent.VK_UP, "up2");
    playerOneAndPlayerTwoControls.put(KeyEvent.VK_A, "left1");
    playerOneAndPlayerTwoControls.put(KeyEvent.VK_S, "down1");
    playerOneAndPlayerTwoControls.put(KeyEvent.VK_D, "right1");
    playerOneAndPlayerTwoControls.put(KeyEvent.VK_W, "up1");

    explosionFrames = new Image[]{
      imageGenerator.getImage("Images/explosion1_1.png"),
      imageGenerator.getImage("Images/explosion1_2.png"),
      imageGenerator.getImage("Images/explosion1_3.png"),
      imageGenerator.getImage("Images/explosion1_4.png"),
      imageGenerator.getImage("Images/explosion1_5.png"),
      imageGenerator.getImage("Images/explosion1_6.png")
    };
    Image bullet = imageGenerator.getImage("Images/bullet.png");

    playerOneBullets = new ArrayList();
    playerTwoBullets = new ArrayList();
    playerOneTank = new Tank("Images/Tank_blue_heavy_strip60.png", playerTwoBullets, playerOneBullets, bullet, 1);
    playerTwoTank = new Tank("Images/Tank_blue_heavy_strip60.png", playerOneBullets, playerTwoBullets, bullet, 2);


    player[1] = playerOneTank;
    player[2] = playerTwoTank;

    tankWorldEvents = new TankWorldEvents();

    tankWorldEvents.addObserver(playerOneTank);
    tankWorldEvents.addObserver(playerTwoTank);

    playerControls = new PlayerControls(tankWorldEvents);

    addKeyListener(playerControls);
  }


  public void paint(Graphics graphics){

    Dimension dimension = getSize();
    updateDisplay();
    updatePlayerOneDisplay();
    updatePlayerTwoDisplay();


    bufferedImage2 = ( BufferedImage ) createImage(GAME_BOARD_WIDTH, GAME_BOARD_HEIGHT );
    Graphics2D g3 = bufferedImage2.createGraphics();
    g3.setBackground(getBackground());
    g3.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);

    g3.clearRect(0, 0, GAME_BOARD_WIDTH, GAME_BOARD_HEIGHT);


    // draws player 1's screen.
    g3.drawImage(bufferedImage.getSubimage(playerOneXDisplay, playerOneYDisplay, APPLET_WIDTH / 2, APPLET_HEIGHT), 0, 0, this);

    // draw player 2's screen.
    g3.drawImage(bufferedImage.getSubimage(playerTwoXDisplay, playerTwoYDisplay, APPLET_WIDTH / 2, APPLET_HEIGHT), APPLET_WIDTH / 2, 0, this);

    // draw minimap.
    g3.drawImage(bufferedImage.getScaledInstance(dimension.width / 5, dimension.height / 5, 1), dimension.width / 2 - (dimension.width / 5) / 2, dimension.height * 3 / 4, this);

    // draw the mini map dividing the two players. UNECESSARY!?
    g3.drawLine(dimension.width / 2 + 2, 0, dimension.width / 2 + 2, dimension.height);

    g3.dispose();

    graphics.drawImage(bufferedImage2,0,0,this);
  }


  private void updatePlayerOneDisplay(){
    playerOneXDisplay = playerOneTank.x + 30 - APPLET_WIDTH / 4;
    if (playerOneXDisplay < 0) {
      playerOneXDisplay = 0;
    } else if (playerOneXDisplay + APPLET_WIDTH / 2 > GAME_BOARD_WIDTH) {
      playerOneXDisplay = GAME_BOARD_WIDTH - APPLET_WIDTH / 2;
    }
    playerOneYDisplay = playerOneTank.y + 30 - APPLET_HEIGHT / 2;
    if (playerOneYDisplay < 0) {
      playerOneYDisplay = 0;
    } else if (playerOneYDisplay + APPLET_HEIGHT > GAME_BOARD_HEIGHT) {
      playerOneYDisplay = GAME_BOARD_HEIGHT - APPLET_HEIGHT;
    }
  }


  private void updatePlayerTwoDisplay(){
    playerTwoXDisplay = playerTwoTank.x + 30 - APPLET_WIDTH / 4;
    if (playerTwoXDisplay < 0) {
      playerTwoXDisplay = 0;
    } else if (playerTwoXDisplay + APPLET_WIDTH / 2 > GAME_BOARD_WIDTH) {
      playerTwoXDisplay = GAME_BOARD_WIDTH - APPLET_WIDTH / 2;
    }
    playerTwoYDisplay = playerTwoTank.y + 30 - APPLET_HEIGHT / 2;
    if (playerTwoYDisplay < 0) {
      playerTwoYDisplay = 0;
    } else if (playerTwoYDisplay + APPLET_HEIGHT > GAME_BOARD_HEIGHT) {
      playerTwoYDisplay = GAME_BOARD_HEIGHT - APPLET_HEIGHT;
    }

  }

  public void updateDisplay(){
    bufferedImage = (BufferedImage) createImage( GAME_BOARD_WIDTH, GAME_BOARD_HEIGHT );
    graphics2D = bufferedImage.createGraphics();
    graphics2D.setBackground(getBackground());
    graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,
                                RenderingHints.VALUE_RENDER_QUALITY);
    graphics2D.clearRect(0,0,GAME_BOARD_WIDTH,GAME_BOARD_HEIGHT);

    gameBoard.draw(graphics2D, this);

    playerOneTank.move();
    playerOneTank.drawVehicle(graphics2D, this);
    playerTwoTank.move();
    playerTwoTank.drawVehicle(graphics2D, this);



    for (int i = 0; i < playerOneBullets.size(); i++) {
      playerOneBullets.get(i).moveBullet();
      playerTwoBullets.get(i).drawBullet(graphics2D, this);
    }

    for (int i = 0; i < playerTwoBullets.size(); i++) {
      playerTwoBullets.get(i).moveBullet();
      playerTwoBullets.get(i).drawBullet(graphics2D, this);

    }

  }
}
