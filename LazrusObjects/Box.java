package LazrusObjects;

import java.awt.*;

/**
 * Created by ericgumba on 4/27/17.
 */
public abstract class Box extends LazarusWorld {

  int weight;
  Image boxImage;
  int xLocation;
  int yLocation;
  int ySpeed;
  boolean isVisible = true;

  // useful information about the boxes
  final int BOX_WIDTH = 40;
  final int BOX_HEIGHT = 40;


  public abstract int weight();

  public void draw(Graphics g){
    if ( isVisible ){
      g.drawImage(boxImage, xLocation, yLocation, observer);
    }
  }

  public void update(){
  }
  public boolean collision(int xPositionOfObject, int yPositionOfObject, int widthOfObject, int heightOfObject){
    if ( isVisible ) {
      if ( yPositionOfObject + heightOfObject > BOX_HEIGHT && yPositionOfObject < yLocation + BOX_HEIGHT ){
        if ( xPositionOfObject + widthOfObject > xLocation && xPositionOfObject < xLocation + BOX_WIDTH ){
          return true;
        }
      }
    }
    return false;
  }
  public void move(){
    yLocation += ySpeed;
  }
}
