package LazrusObjects;
import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.*;
import static java.applet.Applet.newAudioClip;

/**
 * Created by ericgumba on 4/26/17.
 */
public class LazarusWorld extends JPanel implements Runnable {

  static java.util.List<Stack<Box>> boxWeights;
  private BufferedImage bufferedImg, bufferedImg2;
  static ImageGenerator imgGen;
  LazarusBackground lazBackground;
  public ImageObserver observer;
  final int GAMEBOARD_WIDTH = 640, GAMEBOARD_HEIGHT = 480;
  LazarusEvents lazEvents;
  LazarusControls lazControls;
  static AudioClip deathOfLazarus, lazarusMoved, boxCrushed, buttonPressed, music;

  static int currentLevel = 1;
  static MainCharacter mc;
  static HashMap<Integer, String> controls;
  Thread thread;
  static BoxGenerator boxGen;
  static HashMap<Integer, Integer> boxPositions;
  int timeCounter = 200;
  static Stack<Box> nextBox = new Stack<Box>();
  static HashMap<Integer, Box> boxTypes;
  int currentBoxSpeed = 9;
  BufferedImage leftJumpStrip, afraidStrip, squishedStrip;
  BufferedImage[] leftJumpFrame, afraidFrame, squishedFrame;

  int jumpTimer = 0, afraidTimer = 0, squishTimer = 0;
  int boxDecider = (int)(Math.random() * ((4 - 1) + 1) + 1);

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




    try {
      deathOfLazarus = newAudioClip(LazarusWorld.class.getResource( "Lazarus/Squished.wav" ));
      lazarusMoved = newAudioClip(LazarusWorld.class.getResource( "Lazarus/Move.wav" ));
      boxCrushed = newAudioClip(LazarusWorld.class.getResource("Lazarus/Crush.wav"));
      buttonPressed = newAudioClip(LazarusWorld.class.getResource("Lazarus/Button.wav"));
      music = newAudioClip(LazarusWorld.class.getResource("Lazarus/Music.mid"));

    } catch (Exception e){
      System.out.println("Cannot get audio.");
    }

    // initialize thread.
    thread = new Thread(this);
    thread.setPriority(Thread.MIN_PRIORITY);
    thread.start();

    music.play();
    // Create data structures
    boxPositions = new HashMap<>();
    controls = new HashMap<>();
    boxWeights = new ArrayList<Stack<Box>>();
    boxTypes = new HashMap<>();

    // Initialize data structures
    controls.put( KeyEvent.VK_LEFT, "left" );
    controls.put( KeyEvent.VK_RIGHT, "right" );
    controls.put( KeyEvent.VK_SPACE, "space" );

    for(int i = 0; i < 16; i++) {
      boxPositions.put(i*40, i);
    }
    for ( int i = 0; i < 16; i++){
      boxWeights.add( new Stack< Box >());
    }
    imgGen = new ImageGenerator();
    boxGen = new BoxGenerator();

    try {
      leftJumpStrip = imgGen.getBufferedImage("Lazarus/Lazarus_left_strip7.png");
      afraidStrip = imgGen.getBufferedImage("Lazarus/Lazarus_afraid_strip10.png");
      squishedStrip = imgGen.getBufferedImage("Lazarus/Lazarus_squished_strip11.png");
    } catch ( Exception e){ System.out.println("?");}

    leftJumpFrame = new BufferedImage[] {
        leftJumpStrip.getSubimage(10,0,70,80),
        leftJumpStrip.getSubimage(80,0,70,80),
        leftJumpStrip.getSubimage(150,0,70,80),
        leftJumpStrip.getSubimage(220,0,70,80),
        leftJumpStrip.getSubimage(290,0,70,80),
        leftJumpStrip.getSubimage(370,0,70,80),
        leftJumpStrip.getSubimage(450,0,70,80),
        };

    afraidFrame = new BufferedImage[] {
        afraidStrip.getSubimage(0,0,40,40),
        afraidStrip.getSubimage(40,0,40,40),
        afraidStrip.getSubimage(80,0,40,40),
        afraidStrip.getSubimage(120,0,40,40),
        afraidStrip.getSubimage(160,0,40,40),
        afraidStrip.getSubimage(200,0,40,40),
        afraidStrip.getSubimage(240,0,40,40),
        afraidStrip.getSubimage(280,0,40,40),
        afraidStrip.getSubimage(320,0,40,40),
        afraidStrip.getSubimage(360,0,40,40),
    };
    squishedFrame = new BufferedImage[] {
        squishedStrip.getSubimage(0,0,40,40),
        squishedStrip.getSubimage(40,0,40,40),
        squishedStrip.getSubimage(80,0,40,40),
        squishedStrip.getSubimage(120,0,40,40),
        squishedStrip.getSubimage(160,0,40,40),
        squishedStrip.getSubimage(200,0,40,40),
        squishedStrip.getSubimage(240,0,40,40),
        squishedStrip.getSubimage(280,0,40,40),
        squishedStrip.getSubimage(320,0,40,40),
        squishedStrip.getSubimage(360,0,40,40),
        squishedStrip.getSubimage(400,0,40,40),
    };
    mc = new MainCharacter();

    lazBackground = new LazarusBackground();
    this.setFocusable( true );
    observer = this;


    lazEvents = new LazarusEvents();
    lazEvents.addObserver( mc );
    lazControls = new LazarusControls( lazEvents );
    addKeyListener( lazControls );

  }
  public void paint( Graphics g ){



    if(mc.getLazarusPosition() != 0
        && mc.getLazarusPosition() != 15
        && !mc.lazarusIsSquished
        ) {
      updateAndDisplay();


      bufferedImg2 = (BufferedImage) createImage(GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT);
      Graphics2D g3 = bufferedImg2.createGraphics();
      g3.setBackground(getBackground());
      g3.setRenderingHint(RenderingHints.KEY_RENDERING,
          RenderingHints.VALUE_RENDER_QUALITY);
      g3.clearRect(0, 0, GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT);
      g3.drawImage(bufferedImg.getSubimage(0, 0, GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT), 0, 0, this);
      g3.dispose();
      g.drawImage(bufferedImg2, 0, 0, this);

      if ( !mc.lazarusIsMoving && mc.lazarusCanMove ) {
        g.drawImage(mc.getImageOfLazarus(), mc.getxLocation(), mc.getyLocation(), this);

        if ( !mc.collision( mc.getxLocation(),
            boxWeights.get(boxPositions.get( mc.xLocation )).peek().yLocation - 7, 40)) {
          fall();
        }
         if ( mc.lazarusIsAfraid() ){
          afraidTimer++;
          switch ( afraidTimer ){
            case 1:
              g.drawImage(afraidFrame[afraidTimer-1], mc.getxLocation(), mc.getyLocation(), this);
            case 2:
              g.drawImage(afraidFrame[afraidTimer-1], mc.getxLocation(), mc.getyLocation(), this);
            case 3:
              g.drawImage(afraidFrame[afraidTimer-1], mc.getxLocation(), mc.getyLocation(), this);
            case 4:
              g.drawImage(afraidFrame[afraidTimer-1], mc.getxLocation(), mc.getyLocation(), this);
            case 5:
              g.drawImage(afraidFrame[afraidTimer-1], mc.getxLocation(), mc.getyLocation(), this);
            case 6:
              g.drawImage(afraidFrame[afraidTimer-1], mc.getxLocation(), mc.getyLocation(), this);
            case 7:
              g.drawImage(afraidFrame[afraidTimer-1], mc.getxLocation(), mc.getyLocation(), this);
            case 8:
              g.drawImage(afraidFrame[afraidTimer-1], mc.getxLocation(), mc.getyLocation(), this);
            case 9:
              g.drawImage(afraidFrame[afraidTimer-1], mc.getxLocation(), mc.getyLocation(), this);
            case 10:
              g.drawImage(afraidFrame[afraidTimer-1], mc.getxLocation(), mc.getyLocation(), this);
          }
          if ( afraidTimer >= 10 ){
            afraidTimer = 0;
          }
        }


      } else if(! mc.lazarusCanMove ) {

        squishTimer++;
        switch (squishTimer) {
          case 1:
            g.drawImage(squishedFrame[squishTimer - 1], mc.getxLocation(), mc.getyLocation(), this);
          case 2:
            g.drawImage(squishedFrame[squishTimer - 1], mc.getxLocation(), mc.getyLocation(), this);
          case 3:
            g.drawImage(squishedFrame[squishTimer - 1], mc.getxLocation(), mc.getyLocation(), this);
          case 4:
            g.drawImage(squishedFrame[squishTimer - 1], mc.getxLocation(), mc.getyLocation(), this);
          case 5:
            g.drawImage(squishedFrame[squishTimer - 1], mc.getxLocation(), mc.getyLocation(), this);
          case 6:
            g.drawImage(squishedFrame[squishTimer - 1], mc.getxLocation(), mc.getyLocation(), this);
          case 7:
            g.drawImage(squishedFrame[squishTimer - 1], mc.getxLocation(), mc.getyLocation(), this);
          case 8:
            g.drawImage(squishedFrame[squishTimer - 1], mc.getxLocation(), mc.getyLocation(), this);
          case 9:
            g.drawImage(squishedFrame[squishTimer - 1], mc.getxLocation(), mc.getyLocation(), this);
          case 10:
            g.drawImage(squishedFrame[squishTimer - 1], mc.getxLocation(), mc.getyLocation(), this);
        }

        if (squishTimer >= 10) {
          squishTimer = 0;

          deathOfLazarus.play();
          reset();
        }
      }else if ( mc.lazarusIsMovingLeft ) {
        jumpTimer++;
        switch (jumpTimer){
          case 1:
            g.drawImage(leftJumpFrame[jumpTimer-1], mc.getxLocation()-5*(jumpTimer), mc.getyLocation() - 40, this);
          case 2:
            g.drawImage(leftJumpFrame[jumpTimer-1], mc.getxLocation()-5*(jumpTimer), mc.getyLocation() - 40, this);
          case 3:
            g.drawImage(leftJumpFrame[jumpTimer-1], mc.getxLocation()-5*(jumpTimer), mc.getyLocation() - 40, this);
          case 4:
            g.drawImage(leftJumpFrame[jumpTimer-1], mc.getxLocation()-5*(jumpTimer), mc.getyLocation() - 40, this);
          case 5:
            g.drawImage(leftJumpFrame[jumpTimer-1], mc.getxLocation()-5*(jumpTimer), mc.getyLocation() - 40, this);
          case 6:
            g.drawImage(leftJumpFrame[jumpTimer-1], mc.getxLocation()-5*(jumpTimer), mc.getyLocation() - 40, this);
          case 7:
            g.drawImage(leftJumpFrame[jumpTimer-1], mc.getxLocation()-5*(jumpTimer), mc.getyLocation() - 40, this);
        }

        if ( jumpTimer >= 7 ) {
          mc.setLazarusIsMoving(false);
          mc.setLazarusIsMovingLeft(false);
          jumpTimer = 0;
        }

      } else if ( mc.lazarusIsMovingRight ) {
        jumpTimer++;
        switch (jumpTimer){
          case 1:
            g.drawImage(leftJumpFrame[jumpTimer-1], (mc.getxLocation()-70)+5*(jumpTimer), mc.getyLocation() - 40, this);
          case 2:
            g.drawImage(leftJumpFrame[jumpTimer-1], (mc.getxLocation()-70)+5*(jumpTimer), mc.getyLocation() - 40, this);
          case 3:
            g.drawImage(leftJumpFrame[jumpTimer-1], (mc.getxLocation()-70)+5*(jumpTimer), mc.getyLocation() - 40, this);
          case 4:
            g.drawImage(leftJumpFrame[jumpTimer-1], (mc.getxLocation()-70)+5*(jumpTimer), mc.getyLocation() - 40, this);
          case 5:
            g.drawImage(leftJumpFrame[jumpTimer-1], (mc.getxLocation()-70)+5*(jumpTimer), mc.getyLocation() - 40, this);
          case 6:
            g.drawImage(leftJumpFrame[jumpTimer-1], (mc.getxLocation()-70)+5*(jumpTimer), mc.getyLocation() - 40, this);
          case 7:
            g.drawImage(leftJumpFrame[jumpTimer-1], (mc.getxLocation()-70)+5*(jumpTimer), mc.getyLocation() - 40, this);
        }
        if ( jumpTimer >= 7 ) {
          mc.setLazarusIsMoving(false);
          mc.setLazarusIsMovingRight(false);
          jumpTimer = 0;
        }
      }

      timeCounter++;

      // problem: figure out how to pop from the stack to save memory
      if ( timeCounter >= 100 ) {
        System.out.println( "generating box: " + boxDecider );
        timeCounter = 0;
        boxGen.addBox( mc.getLazarusPosition() * 40, 0, boxDecider, currentBoxSpeed );
        boxDecider = (int) (Math.random() * ((4 - 1) + 1) + 1);
        nextBox.push(boxTypes.get( boxDecider ));
      }

      g.drawImage( nextBox.peek().getBoxImage(),
          nextBox.peek().getxLocation(),
          nextBox.peek().getyLocation(),
          this );


    } else if ( mc.lazarusIsSquished ){

      reset();
    }

    else{
      buttonPressed.play();
      currentBoxSpeed += 2;
      currentLevel += 1;
      reset();
    }

  }

  //resets the state of the data structures holding the boxes, the main character
  // and the box generator.
  public void reset(){

    boxPositions = new HashMap<>();
    boxWeights = new ArrayList<Stack<Box>>();
    for(int i = 0; i < 16; i++) {
      boxPositions.put(i*40, i);
    }
    for ( int i = 0; i < 16; i++){
      boxWeights.add( new Stack< Box >());
    }
    boxGen = new BoxGenerator();

    mc.resetLazarusPosition();

  }

  public void updateAndDisplay(){
    bufferedImg = ( BufferedImage ) createImage( GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT ); // create image that is x by y
    Graphics2D gameGraphics = bufferedImg.createGraphics();
    gameGraphics.setBackground( getBackground() );
    gameGraphics.setRenderingHint( RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY );
    gameGraphics.clearRect(0,0,GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT);
    lazBackground.draw(gameGraphics, this);
    try {
      boxGen.draw(gameGraphics, this);
    } catch (Exception e){boxGen.draw(gameGraphics, this);}
  }
  public void fall(){
    mc.setyLocation( mc.getyLocation() + 9 );
    mc.setyMove( mc.getyLocation() );
  }

}
