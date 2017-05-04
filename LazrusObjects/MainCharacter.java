package LazrusObjects;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Random;

/**
 * Created by ericgumba on 4/27/17.
 */
public class MainCharacter extends Box implements MainCharacterInterface {
  int xLocation, yLocation;
  Image imageOfLazarus;
  int xMove, yMove;
  int lazarusPosition;
  static boolean lazarusIsSquished = false;
  MainCharacter(){
    lazarusPosition = 5;
    xMove = 200;
    yMove = GAMEBOARD_HEIGHT-145;
    xLocation = xMove;
    yLocation = yMove;
    imageOfLazarus = imgGen.getImage( "Lazarus/Lazarus_stand.png" );
  }

  public boolean collision(int xLocation, int y, int lazarusHeight){

    if ( y + lazarusHeight > yLocation
        && y < yLocation + lazarusHeight
        && xLocation == this.xLocation
        ){
      return true;
    }
    return false;
  }
  public void move(){
    xLocation = xMove;
    yLocation = yMove;
  }

  public void setLazarusIsSquished(boolean lazarusIsSquished) {
    this.lazarusIsSquished = lazarusIsSquished;
    System.out.println("L is squished");
  }

  public void resetLazarusPosition( ) {
    xMove = 200;
    yMove = GAMEBOARD_HEIGHT-145;
    this.xLocation = 200;
    this.yLocation = GAMEBOARD_HEIGHT-145;
    this.lazarusPosition = 5;
    lazarusIsSquished = false;

  }

  public int getxLocation() {
    return xLocation;
  }

  public int getyLocation() {
    return yLocation;
  }

  public Image getImageOfLazarus() {
    return imageOfLazarus;
  }

  public int getLazarusPosition() {
    return lazarusPosition;
  }



  @Override
  public int weight() {
    return 0;
  }

  // is called everytime an action happens.
  public void update( Observable obj, Object event ){


    LazarusEvents lazE = ( LazarusEvents ) event;
    if( lazE.eventType == 0 ){
      KeyEvent keyevnt = ( KeyEvent ) lazE.event;
      String lazAction = controls.get( keyevnt.getKeyCode() );
      if ( lazAction.equals( "left" ) ){
        if (  boxWeights.get( lazarusPosition - 1 ).size() - boxWeights.get( lazarusPosition ).size() < 2) {
          xMove -= 40;
          yMove -= 40 * ( boxWeights.get( lazarusPosition - 1 ).size() - boxWeights.get( lazarusPosition ).size() );
          lazarusPosition -= 1;
          System.out.println( "laz pos: " + lazarusPosition );
          move();
        }
      } else if ( lazAction.equals( "right" ) ){
        if ( boxWeights.get( lazarusPosition + 1 ).size() - boxWeights.get( lazarusPosition ).size() < 2) {
          xMove += 40;
          yMove -= 40 * ( boxWeights.get( lazarusPosition + 1 ).size() - boxWeights.get( lazarusPosition ).size() );
          lazarusPosition += 1;

          System.out.println("Laz pos: " + lazarusPosition);
          move();
        }
      }
    }
  }
}

