package LazrusObjects;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
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
  static boolean lazarusIsSquished = false; // needs to be static?
  static boolean lazarusCanMove = true;
  static boolean lazarusIsMoving = false;
  static boolean lazarusIsMovingLeft = false;
  static boolean lazarusIsMovingRight = false;

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
    lazarusIsMoving = true;
    xLocation = xMove;
    yLocation = yMove;
    lazarusMoved.play();
  }

  public static void setLazarusIsMoving(boolean lazarusIsMoving) {
    MainCharacter.lazarusIsMoving = lazarusIsMoving;
  }

  public static void setLazarusIsMovingLeft(boolean lazarusIsMovingLeft) {
    MainCharacter.lazarusIsMovingLeft = lazarusIsMovingLeft;
  }

  public static void setLazarusIsMovingRight(boolean lazarusIsMovingRight) {
    MainCharacter.lazarusIsMovingRight = lazarusIsMovingRight;
  }

  public void resetLazarusPosition( ) {
    xMove = 200;
    yMove = GAMEBOARD_HEIGHT-145;
    this.xLocation = 200;
    this.yLocation = GAMEBOARD_HEIGHT-145;
    this.lazarusPosition = 5;
    lazarusIsSquished = false;
    lazarusCanMove = true;

  }

  public int getxLocation() {
    return xLocation;
  }

  public int getyLocation() {
    return yLocation;
  }

  public void setyLocation(int yLocation) {
    this.yLocation = yLocation;
  }

  public int getyMove() {
    return yMove;
  }

  public void setyMove(int yMove) {
    this.yMove = yMove;
  }

  public Image getImageOfLazarus() {
    return imageOfLazarus;
  }

  public int getLazarusPosition() {
    return lazarusPosition;
  }


  public void setLazarusIsSquished(boolean lazarusIsSquished) {
    this.lazarusIsSquished = lazarusIsSquished;
    deathOfLazarus.play();
  }

  public static void setLazarusCanMove(boolean lazarusCanMove) {
    MainCharacter.lazarusCanMove = lazarusCanMove;
  }

  @Override
  public int weight() {
    return 0;
  }



  // is called everytime an action happens.
  public void update( Observable obj, Object event ){
    LazarusEvents lazE = ( LazarusEvents ) event;
    if ( lazarusCanMove == true ) {
      if (lazE.eventType == 0) {
        KeyEvent keyevnt = (KeyEvent) lazE.event;
        String lazAction = controls.get(keyevnt.getKeyCode());
        if (lazAction.equals("left")) {
          if (boxWeights.get(lazarusPosition - 1).size() - boxWeights.get(lazarusPosition).size() < 2) {
            xMove -= 40;

            if (boxWeights.get(lazarusPosition - 1).size() - boxWeights.get(lazarusPosition).size() == 1) {
              yMove -= 40;
            }
            move();
            lazarusPosition -= 1;
            setLazarusIsMovingLeft(true);
          }
        } else if (lazAction.equals("right")) {
          if (boxWeights.get(lazarusPosition + 1).size() - boxWeights.get(lazarusPosition).size() < 2) {
            xMove += 40;

            if (boxWeights.get(lazarusPosition + 1).size() - boxWeights.get(lazarusPosition).size() == 1) {
              yMove -= 40;
            }

            lazarusPosition += 1;
            move();

            setLazarusIsMovingRight(true);
          }
        }
      }
    }
  }

  public boolean lazarusIsAfraid() {

    ArrayList<Box> box = boxGen.getBox();
    for (Box b : box) {
      if (b.getxLocation() == mc.getxLocation()) {

        return true;
      }
    }

    return false;
  }
}

