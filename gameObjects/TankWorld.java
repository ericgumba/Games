package gameObjects;

import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JApplet;
/**
 * Created by ericgumba and Leo Wang on 4/20/17.
 */
public class TankWorld extends JApplet {

  static ImageGenerator imageGenerator;
  static HashMap<Integer, String> controls = new HashMap<>();
  final int BACKGROUND_WIDTH = 1475, BACKGROUND_HEIGHT = 1155;
  private static int displayX1, displayY1, displayX2, displayY2;
  private final int SCREEN_WIDTH = 840, SCREEN_HEIGHT = 880;
  ImageObserver observer;
  private BufferedImage bufferedImg;
  TankWorldEvents tankWorldEvents;
  private PlayerControls gameControls;
  Graphics2D g2;
  //Game Objects
  Background theBackground;
  static Tank tankL, tankR;
//  static RandomObjectGenerator ObjectManager;
  static Tank[] player = new Tank[3];
  static Image[] explosionFrames;
  static ArrayList<Bullet> tankLBullets, tankRBullets;
  static AudioClip fire, death;
  static WallGenerator wallGenerator;


  /**
   * Initializes various objects within the game, such as the tanks,
   * bullets, image generators, and wall generators.
   */
  public void init() {

    setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    imageGenerator = new ImageGenerator();
    wallGenerator = new WallGenerator();

    initializePlayerOneAndPlayerTwoControls();


    Image bullets = imageGenerator.getImage("Images/bullet.png") ;

    // Initializes the explosion frames used when a tank dies.
    explosionFrames = new Image[]{
        imageGenerator.getImage("Images/explosion1_1.png"),
        imageGenerator.getImage("Images/explosion1_2.png"),
        imageGenerator.getImage("Images/explosion1_3.png"),
        imageGenerator.getImage("Images/explosion1_4.png"),
        imageGenerator.getImage("Images/explosion1_5.png"),
        imageGenerator.getImage("Images/explosion1_6.png"),
        imageGenerator.getImage("Images/explosion1_6.png")
    };

    //Game Objects:
    theBackground = new Background();


    tankLBullets = new ArrayList();
    tankRBullets = new ArrayList();

    tankL = new Tank("Images/Tank_blue_heavy_strip60.png", tankRBullets, tankLBullets, bullets, 1);
    tankR = new Tank("Images/Tank_blue_heavy_strip60.png", tankLBullets, tankRBullets, bullets, 2);

    // index 0 won't be used.
    player[1] = tankR;
    player[2] = tankL;

    this.setFocusable(true);
    observer = this;
    tankWorldEvents = new TankWorldEvents();


    tankWorldEvents.addObserver(tankL);
    tankWorldEvents.addObserver(tankR);

    gameControls = new PlayerControls(tankWorldEvents);
    addKeyListener(gameControls);

//    explosionSound_1 = getAudioFile("Resources/Explosion_large.wav");
//    explosionSound_2 = getAudioFile("Resources/Explosion_small.wav");
  }

  private AudioClip getAudioFile(String fileName) {
    URL url = TankWorld.class.getResource(fileName);
    return newAudioClip(url);
  }


  public void paint(Graphics g) {

    Dimension d = getSize();

    updateAndDisplay();
    updatePlayerOneDisplay();
    updatePlayerTwoDisplay();


    BufferedImage bufferedImg2 = (BufferedImage) createImage(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    Graphics2D g3 = bufferedImg2.createGraphics();
    g3.setBackground(getBackground());
    g3.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);

    // draws player 1's screen.
    g3.drawImage(bufferedImg.getSubimage(displayX1, displayY1, SCREEN_WIDTH / 2, SCREEN_HEIGHT), 0, 0, this);

    // draw player 2's screen.
    g3.drawImage(bufferedImg.getSubimage(displayX2, displayY2, SCREEN_WIDTH / 2, SCREEN_HEIGHT), SCREEN_WIDTH / 2, 0, this);

    // draw minimap.
    g3.drawImage(bufferedImg.getScaledInstance(d.width / 5, d.height / 5, 1), d.width / 2 - (d.width / 5) / 2, d.height * 3 / 4, this);

    // draw the mini map dividing the two players. UNECESSARY!?
    g3.drawLine(d.width / 2 + 2, 0, d.width / 2 + 2, d.height);

    g3.dispose();
    g.drawImage(bufferedImg2, 0, 0, this); // x = 0, y = 0 means the image is at the top left.


  }
  public void updateAndDisplay( ) {
    bufferedImg = (BufferedImage) createImage(BACKGROUND_WIDTH, BACKGROUND_HEIGHT); // create image that is x by y
    g2 = bufferedImg.createGraphics();
    g2.setBackground(getBackground());
    g2.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    g2.clearRect(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

    theBackground.draw(g2, this);


    tankL.move();
    tankL.draw(g2, this);
    tankR.move();
    tankR.draw(g2, this);

    wallGenerator.draw(g2, this);

    for (int i = 0; i < tankLBullets.size(); i++) {
      tankLBullets.get(i).draw(g2, this);
      tankLBullets.get(i).move();
    }

    for (int i = 0; i < tankRBullets.size(); i++) {
      tankRBullets.get(i).draw(g2, this);
      tankRBullets.get(i).move();

    }

  }
  private void updatePlayerOneDisplay(){

    displayX1 = tankL.x + 30 - SCREEN_WIDTH / 4;
    if (displayX1 < 0) {
      displayX1 = 0;
    } else if (displayX1 + SCREEN_WIDTH / 2 > BACKGROUND_WIDTH) {
      displayX1 = BACKGROUND_WIDTH - SCREEN_WIDTH / 2;
    }
    displayY1 = tankL.y + 30 - SCREEN_HEIGHT / 2;
    if (displayY1 < 0) {
      displayY1 = 0;
    } else if (displayY1 + SCREEN_HEIGHT > BACKGROUND_HEIGHT) {
      displayY1 = BACKGROUND_HEIGHT - SCREEN_HEIGHT;
    }
  }

  private void updatePlayerTwoDisplay(){

    displayX2 = tankR.x + 30 - SCREEN_WIDTH / 4;
    if (displayX2 < 0) {
      displayX2 = 0;
    } else if (displayX2 + SCREEN_WIDTH / 2 > BACKGROUND_WIDTH) {
      displayX2 = BACKGROUND_WIDTH - SCREEN_WIDTH / 2;
    }
    displayY2 = tankR.y + 30 - SCREEN_HEIGHT / 2;
    if (displayY2 < 0) {
      displayY2 = 0;
    } else if (displayY2 + SCREEN_HEIGHT > BACKGROUND_HEIGHT) {
      displayY2 = BACKGROUND_HEIGHT - SCREEN_HEIGHT;
    }

  }
  private void initializePlayerOneAndPlayerTwoControls(){

    controls.put(KeyEvent.VK_LEFT, "left2");
    controls.put(KeyEvent.VK_UP, "up2");
    controls.put(KeyEvent.VK_DOWN, "down2");
    controls.put(KeyEvent.VK_RIGHT, "right2");
    controls.put(KeyEvent.VK_ENTER, "shoot2");
    controls.put(KeyEvent.VK_A, "left1");
    controls.put(KeyEvent.VK_W, "up1");
    controls.put(KeyEvent.VK_S, "down1");
    controls.put(KeyEvent.VK_D, "right1");
    controls.put(KeyEvent.VK_SPACE, "shoot1");
  }

}
