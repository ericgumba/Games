package LazrusObjects;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by ericgumba on 4/27/17.
 */
public abstract class Box extends LazarusWorld implements Observer {

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

  public void draw(Graphics g, ImageObserver observer){
    if ( isVisible ){
      g.drawImage(boxImage, xLocation, yLocation, observer);
    }
  }

  @Override
  public void update(Observable o, Object arg) {

  }

  public boolean collision(int xPositionOfObject, int yPositionOfObject, int widthOfObject, int heightOfObject){
    if ( isVisible ) {
      if ( boxWeights.get(boxPositions.get(xLocation)).peek().getyLocation() - 100 == yLocation ){
        return true;
      }
    }
    return false;
  }
  public void move(){
    if (yLocation != boxWeights.get(boxPositions.get(xLocation)).peek().getyLocation() -40 ) {
      yLocation += ySpeed;
    }
  }

  public int getxLocation(){
    return xLocation;
  }
  public int getyLocation(){
    return yLocation;
  }
  public int getBOX_WIDTH(){
    return BOX_WIDTH;
  }
}
