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
  final int borderX = 1500, borderY = 1200; //objects need to know the border
  static int screenWidth = 840, screenHeight = 880, displayX1, displayY1, displayX2, displayY2;
  ImageObserver observer;
  BufferedImage bufferedImg, bufferedImg2;
  TankWorldEvents tankWorldEvents;
  PlayerControls gC;
  Graphics2D g2;
  //Game Objects
  Background theBackground;
  static Tank tankL, tankR;
//  static RandomObjectGenerator ObjectManager;
  static Tank[] player = new Tank[3];
  static Image[] explosionFrames;
  static ArrayList<Bullet> tankLBullets, tankRBullets;
  static AudioClip explosionSound_1, explosionSound_2;
  static WallGenerator wallGenerator;


  public static void main(String[] args) { final TankWorld preAlpha = new TankWorld();}

  public void init() {

    setSize(screenWidth,screenHeight); // check

    imageGenerator = new ImageGenerator(); //check

    wallGenerator = new WallGenerator();

    initializePlayerOneAndPlayerTwoControls(); // check


    Image bullets = imageGenerator.getImage("Images/bullet.png") ;

    // health info images
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

//    ObjectManager = new RandomObjectGenerator();
    this.setFocusable(true);
    observer = this;
    tankWorldEvents = new TankWorldEvents();

    // adds the controls.... - Eric
    tankWorldEvents.addObserver(tankL);
    tankWorldEvents.addObserver(tankR);

    gC = new PlayerControls(tankWorldEvents);
    addKeyListener(gC);

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


    bufferedImg2 = (BufferedImage) createImage( borderX, borderY);
    Graphics2D g3 = bufferedImg2.createGraphics();
    g3.setBackground(getBackground());
    g3.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);

    // draws player 1's screen.
    g3.drawImage(bufferedImg.getSubimage(displayX1, displayY1, screenWidth / 2, screenHeight), 0, 0, this);

    // draw player 2's screen.
    g3.drawImage(bufferedImg.getSubimage(displayX2, displayY2, screenWidth / 2, screenHeight), screenWidth / 2, 0, this);

    // draw minimap.
    g3.drawImage(bufferedImg.getScaledInstance(d.width / 5, d.height / 5, 1), d.width / 2 - (d.width / 5) / 2, d.height * 3 / 4, this);

    // draw the mini map dividing the two players. UNECESSARY!?
    g3.drawLine(d.width / 2 + 2, 0, d.width / 2 + 2, d.height);

    g3.dispose();
    g.drawImage(bufferedImg2, 0, 0, this); // x = 0, y = 0 means the image is at the top left.


  }
  public void updateAndDisplay( ) {
    bufferedImg = (BufferedImage) createImage(borderX, borderY); // create image that is x by y
    g2 = bufferedImg.createGraphics();
    g2.setBackground(getBackground());
    g2.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    g2.clearRect(0, 0, borderX, borderY);

    theBackground.draw(g2, this);

    tankL.move();
    tankL.draw(g2, this);
    tankR.move();
    tankR.draw(g2, this);

    wallGenerator.draw(g2, this);

    for (int i = 0; i < tankLBullets.size(); i++) {
      tankLBullets.get(i).move();
      tankLBullets.get(i).draw(g2, this);
    }

    for (int i = 0; i < tankRBullets.size(); i++) {
      tankRBullets.get(i).move();
      tankRBullets.get(i).draw(g2, this);

    }

  }
  private void updatePlayerOneDisplay(){

    displayX1 = tankL.x + 30 - screenWidth / 4;
    if (displayX1 < 0) {
      displayX1 = 0;
    } else if (displayX1 + screenWidth / 2 > borderX) {
      displayX1 = borderX - screenWidth / 2;
    }
    displayY1 = tankL.y + 30 - screenHeight / 2;
    if (displayY1 < 0) {
      displayY1 = 0;
    } else if (displayY1 + screenHeight > borderY) {
      displayY1 = borderY - screenHeight;
    }
  }

  private void updatePlayerTwoDisplay(){

    displayX2 = tankR.x + 30 - screenWidth / 4;
    if (displayX2 < 0) {
      displayX2 = 0;
    } else if (displayX2 + screenWidth / 2 > borderX) {
      displayX2 = borderX - screenWidth / 2;
    }
    displayY2 = tankR.y + 30 - screenHeight / 2;
    if (displayY2 < 0) {
      displayY2 = 0;
    } else if (displayY2 + screenHeight > borderY) {
      displayY2 = borderY - screenHeight;
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
