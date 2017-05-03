package LazrusObjects;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Random;

/**
 * Created by ericgumba on 4/27/17.
 */
public class MainCharacter extends LazarusWorld implements MainCharacterInterface {
  int xLocation, yLocation;
  Image imageOfLazarus;
  int xMove = 200, yMove = GAMEBOARD_HEIGHT-145;
  int lazarusPosition = 5;
  MainCharacter(){

    xLocation = xMove;
    yLocation = yMove;
    imageOfLazarus = imgGen.getImage( "Lazarus/Lazarus_stand.png" );

  }
  public void move(){

    xLocation = xMove;
    yLocation = yMove;

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

  // is called everytime an action happens.
  public void update(Observable obj, Object event){

    LazarusEvents lazE = ( LazarusEvents ) event;
    if( lazE.eventType == 0 ){
      KeyEvent keyevnt = ( KeyEvent ) lazE.event;
      String lazAction = controls.get( keyevnt.getKeyCode() );
      if ( lazAction.equals( "left" ) ){
        if (  boxWeights.get( lazarusPosition - 1 ).size() - boxWeights.get( lazarusPosition ).size() < 2) {
          xMove -= 40;
          yMove -= 40 * ( boxWeights.get( lazarusPosition - 1 ).size() - boxWeights.get( lazarusPosition ).size());
          lazarusPosition -= 1;
          move();
        }
      } else if ( lazAction.equals( "right" ) ){
        if ( boxWeights.get( lazarusPosition + 1 ).size() - boxWeights.get( lazarusPosition ).size() < 2) {
          xMove += 40;
          yMove -= 40 * ( boxWeights.get( lazarusPosition + 1 ).size() - boxWeights.get( lazarusPosition ).size() );
          lazarusPosition += 1;
          move();
        }
      }
    }
  }
}

