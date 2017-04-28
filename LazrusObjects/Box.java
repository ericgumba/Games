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
  boolean isCrushed = false;

  // useful information about the boxes
  final int BOX_WIDTH = 40;
  final int BOX_HEIGHT = 40;


  public abstract int weight();

  public void draw(Graphics g){
    if ( !isCrushed ){
      g.drawImage(boxImage, xLocation, yLocation, observer);
    }
  }

  public boolean collision(int xPositionOfObject, int yPositionOfObject, int widthOfObject, int heightOfObject){

    return false;
  }
  public void move(){



  }
}
