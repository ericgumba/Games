package LazrusObjects;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.Observable;

/**
 * Created by ericgumba on 4/27/17.
 */
public class MainCharacter extends LazarusWorld {
  int xLocation, yLocation;
  Image imageOfLazarus;
  int xMove = 10, yMove = 10;
  MainCharacter(){

    imageOfLazarus = imgGen.getImage("Lazarus/Lazarus_stand.png");

    /*
    TO BE REDACTED AT A LATER DATE
     */
    xLocation = 10;
    yLocation = GAMEBOARD_HEIGHT - 90;

  }
  public void move(){

    xLocation = xMove;
    yLocation = yMove;


  }
  public void draw(Graphics g, ImageObserver imObs){
    g.drawImage(imageOfLazarus, xLocation, yLocation, imObs);
  }


  public void update(Observable obj, Object event){
    LazarusEvents lazE = new LazarusEvents();
    if( lazE.eventType <= 1 ){
      KeyEvent keyevnt = (KeyEvent ) lazE.event;
      String lazAction = controls.get(keyevnt.getKeyCode() );
      if ( lazAction.equals("left") ){
        xMove -= 10 * lazE.eventType;
      } else if ( lazAction.equals("right") ){
        xMove += 10*lazE.eventType;
      }
    }
  }
}

