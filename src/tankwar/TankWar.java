package src.tankwar;

import game.GameControl;
import game.GameControlNetwork;
import game.GameEvents;
import game.GameTimer;
import static java.applet.Applet.newAudioClip;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *   @ SFSU Spring 2017 CSC413 Assignment 4 4/18/2017
 */

public class TankWar extends JApplet implements Runnable {

    Integer gameMode; // 0 Single mode 1: server mode 2: client mode
    static HashMap<Integer, String> controls = new HashMap<>();
    static boolean gameIsOver = false;
    static int borderX = 1280, borderY = 1280; //drawing borders
    static int screenWidth =1280, screenHeight = 800, displayX1, displayY1, displayX2, displayY2;
    ImageObserver observer;
    BufferedImage bufferedImg, bufferedImg2;
    private Thread coreThread;
    GameTimer timeKeeper;
    GameEvents gE;
    GameControl gC;
    GameControlNetwork gCN;
    //Game Objects
    Background theBackground;
    static UserTank tankL, tankR;
    static MobileObject ObjectManager;
    static UserTank[] Enemy = new UserTank[3];
    static Image[] healthInfoImages;
    static ArrayList<Bullet> tankLBullets, tankRBullets;
    static AudioClip explosionSound_1, explosionSound_2;
    Font scoreFont = new Font("Impact", Font.PLAIN, 30);
    Font livesFont = new Font("Tahoma", Font.BOLD, 20);
   
    final int levelSize = 40;  // level map size is defined as 40 x 40
    Boolean isServer = true; 
    int startLX, startLY, startRX, startRY;

    public static void main(String[] args) {
        
        // implement network play here?
        /*
        if (args.length()==0) {
            gameMode = 0; // single mode
        };
        System.out.println("gameMode =");
        */
        
        final TankWar tw = new TankWar();
        tw.init();
        JFrame f = new JFrame("2D Tank War Game");
        f.isAlwaysOnTop(); //Leo
        f.addWindowListener(new WindowAdapter() { });
        f.getContentPane().add("Center", tw);
        //f.pack();
        f.setSize(new Dimension(screenWidth, screenHeight));
        f.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        f.setVisible(true);
        f.setResizable(false);
        tw.start();

    }

    //@Override
    public void init() {
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

        //all bullet type images
        Image bullets[] = {getSprite("Resources/enemybullet1.png"),
            getSprite("Resources/enemybullet2.png"), getSprite("Resources/enemybullet3.png")};

        // health info images
        Image[] healthImages = {getSprite("Resources/health1.png"),
            getSprite("Resources/health2.png"),
            getSprite("Resources/health3.png"),
            getSprite("Resources/health4.png"),
            getSprite("Resources/health5.png"),
            getSprite("Resources/explosion1.png"),
            getSprite("Resources/explosion2.png"),
            getSprite("Resources/explosion3.png"),
            getSprite("Resources/explosion4.png"),
            getSprite("Resources/explosion5.png"),
            getSprite("Resources/explosion6.png"),
            getSprite("Resources/explosion7.png")};
        healthInfoImages = healthImages;

        //Game Objects:
        theBackground = new Background();

        ObjectManager = new MobileObject(); // create mobile object(tank and wall) 
        constructWallPattern(); // read level.txt and construct walls initial tank locations
        
        String tankLImages = "Resources/Tank_blue_light_strip60.png";
        String tankRImages = "Resources/Tank_red_light_strip60.png";
        tankLBullets = new ArrayList();
        tankRBullets = new ArrayList();
        tankL = new UserTank(tankLImages, tankRBullets, tankLBullets, bullets, 1, startLX, startLY);
        tankR = new UserTank(tankRImages, tankLBullets, tankRBullets, bullets, 2, startRX, startRY);
        Enemy[1] = tankR;      // index 0 not used
        Enemy[2] = tankL;      // index 0 not used

        setBackground(Color.black);
        this.setFocusable(true);
        observer = this;
        gE = new GameEvents();
        gE.addObserver(tankL);
        gE.addObserver(tankR);
        gE.addObserver(ObjectManager);

        int timedEvents[] = {100, 150, 400, 800};
        timeKeeper = new GameTimer(gE, timedEvents, 1);

        gC = new GameControl(gE);
        addKeyListener(gC);

        explosionSound_1 = getAudioFile("Resources/Explosion_large.wav");
        explosionSound_2 = getAudioFile("Resources/Explosion_small.wav");

    }

    private AudioClip getAudioFile(String fileName) {
        URL url = TankWar.class.getResource(fileName);
        return newAudioClip(url);
    }

    public void start() {
        theBackground.playMusic();
        coreThread = new Thread(this);
        coreThread.setPriority(Thread.MIN_PRIORITY);
        coreThread.start();
    }

    @Override
    public void run() {
        Thread me = Thread.currentThread();
        while (coreThread == me) {
            repaint();
            try {
                coreThread.sleep(20);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        Graphics2D g2 = createGraphics2D(borderX, borderY);
        Graphics2D g3 = createOuterGraphics2D(borderX, borderY);
        updateAndDisplay(borderX, borderY, g2);
        g2.dispose();
        //System.out.println("Dimension d = " + d.width + ", " + d.height);

        //getting tankL's viewing window
        if (!gameIsOver) {
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

            //getting tankR's viewing window
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

            g3.drawImage(bufferedImg.getSubimage(displayX1, displayY1, screenWidth / 2, screenHeight), 0, 0, this);
            g3.drawImage(bufferedImg.getSubimage(displayX2, displayY2, screenWidth / 2, screenHeight), screenWidth / 2, 0, this);
            g3.drawLine(d.width / 2 + 2, 0, d.width / 2 + 2, d.height);
            g3.setFont(scoreFont);
            g3.setColor(Color.BLUE);
            g3.drawString(tankL.score + "", d.width/4-120, d.height-40);
            g3.setColor(Color.RED);
            g3.drawString(tankR.score + "", d.width*3/4-50, d.height-40);
            g3.setFont(livesFont);
            g3.setColor(Color.WHITE);
            g3.drawString("Life: " + tankL.lives + "", d.width/4-40, d.height-40);
            g3.drawString("Life: " + tankR.lives + "", d.width*3/4+40, d.height-40);

            //g3.drawRect(d.width / 2 - (d.width / 5) / 2 - 1, d.height * 3 / 4 - 1, d.width / 5 + 1, d.height / 5 + 1); 
            g3.drawRect (d.width / 2 - (d.width / 5) / 2 - 1, d.height * 3 / 5 - 1, d.width / 5 + 1, d.width / 5 + 1);

            //g3.drawImage(bufferedImg.getScaledInstance(d.height / 5, d.height / 5, 1), d.height / 2 - (d.height / 5) / 2, d.height * 3 / 4, this);
            g3.drawImage(bufferedImg.getScaledInstance(d.width / 5, d.width / 5, 1), d.width / 2 - (d.width / 5) / 2, d.height * 3 / 5, this);

            g3.dispose();            
            g.drawImage(bufferedImg2, 0, 0, this);
        } else {
            g.drawImage(bufferedImg, 0, 0, this);
        }

    }

    public Graphics2D createOuterGraphics2D(int w, int h) {
        Graphics2D g2 = null;
        if (bufferedImg2 == null || bufferedImg2.getWidth() != w || bufferedImg2.getHeight() != h) {
            bufferedImg2 = (BufferedImage) createImage(w, h);
        }
        g2 = bufferedImg2.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    public Graphics2D createGraphics2D(int w, int h) { //bufferdImg was not a parameter before
        Graphics2D g2 = null;
        if (bufferedImg == null || bufferedImg.getWidth() != w || bufferedImg.getHeight() != h) {
            bufferedImg = (BufferedImage) createImage(w, h);
        }
        g2 = bufferedImg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    public void updateAndDisplay(int w, int h, Graphics2D g2) {

        if (!gameIsOver) {
            timeKeeper.play();

            theBackground.draw(g2, this);

            tankL.move();
            tankL.draw(g2, this);

            tankR.move();
            tankR.draw(g2, this);

            ObjectManager.draw(g2, this);


            for (int i = 0; i < tankLBullets.size(); i++) {
                if (tankLBullets.get(i).move()) {
                    tankLBullets.remove(i);
                } else {
                    tankLBullets.get(i).draw(g2, this);
                }
            }

            for (int i = 0; i < tankRBullets.size(); i++) {
                if (tankRBullets.get(i).move()) {
                    tankRBullets.remove(i);
                } else {
                    tankRBullets.get(i).draw(g2, this);
                }
            }
        } else {
            //theBackground.playGameOverMusic();
            theBackground.draw(g2, this);
            //g2.drawImage(getSprite("Resources/GameOver.png"), (borderX / 2) - 125, (borderY / 4) - 40, observer);
            // Drawing the Score in an aligned box:
            //String gameOverMessage = "Game Over";
            String scoreMessage;
            if (tankR.score > tankL.score) {
                scoreMessage = "Red Tank Wins! \nScore: " + tankR.score;
            } else {
                scoreMessage = "Blue Tank Wins! \nScore: " + tankL.score;
            }
            Font l = new Font("Garamond", Font.BOLD, 48);
            g2.setFont(l);

            //measure the message 'greeting'
            FontRenderContext context = g2.getFontRenderContext(); //gets font characteristics specific to screen res.
            Rectangle2D bounds = l.getStringBounds(scoreMessage, context);

            //set (x,y) = top left corner of text rectangle
            double x = (getWidth() - bounds.getWidth()) / 2;
            double y = (getHeight() - bounds.getHeight()) / 2;

            //add ascent to y to reach the baseline
            double ascent = -bounds.getY();
            double baseY = y + ascent;

            g2.setPaint(Color.GREEN);

            //Now, draw the centered, styled message
            //g2.drawString(gameOverMessage, (borderX / 2) - 160, (borderY / 4) - 40);
            g2.drawString(scoreMessage, (int) x, (int) baseY);
        }
    }

    public Image getSprite(String name) {
        URL url = TankWar.class.getResource(name);
        Image img = getToolkit().getImage(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
            System.out.println(e + "the image can't be added");
        }
        return img;
    }

    public BufferedImage getBufferedImage(String name) throws IOException {
        URL url = TankWar.class.getResource(name);
        BufferedImage img = ImageIO.read(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
            System.out.println(e + "the image can't be added");
        }
        return img;
    }
    
    //@SuppressWarnings("null")
    public void constructWallPattern() {            
        BufferedReader source = null;
        char ch;
        String nextLine;
        
        try {
            source = new BufferedReader( new FileReader("tankwar/Resources/level.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MobileObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            for (int i = 0; i<levelSize; i++) {
                nextLine = source.readLine();
                for (int j=0; j<levelSize; j++) {
                    ch = nextLine.charAt(j);
                    //System.out.println( "Char: " + ch + "("+i+","+j+")"+ j*(borderX/40)+" "+i*(borderY/40));
                    if (ch=='1'){
                        ObjectManager.addWall(j*(borderX/40),i*(borderY/40),ch);                       
                    } else if (ch=='2') {
                        ObjectManager.addWall(j*(borderX/40),i*(borderY/40),ch);
                    } else if (ch=='3') {
                        startLX=j*(borderX/40);
                        startLY=i*(borderY/40);
                    } else if (ch=='4') {
                        startRX=j*(borderX/40);
                        startRY=i*(borderY/40);
                    } else if (ch=='5') {
                        // not yet defined in assignment.
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MobileObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if( source != null ) {
             try {
                    source.close();
             } catch( IOException e ) {}
        }
    }
    
    
}
