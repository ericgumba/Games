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
  int ySpeed = 1;

  // useful information about the boxes
  final int BOX_WIDTH = 40;
  final int BOX_HEIGHT = 40;


  public abstract int weight();

  public void draw(Graphics g, ImageObserver observer){
    g.drawImage(boxImage, xLocation, yLocation, observer);
  }

  @Override
  public void update(Observable o, Object arg) {
    System.out.println("test");
  }

  public boolean collision( int y, int height){

    if (  y + height > yLocation
        && y < yLocation + height
        ){

      if( weight > boxWeights.get(boxPositions.get( xLocation )).peek().weight() ){
        boxWeights.get( boxPositions.get( xLocation )).pop();
        return false;
      }
      if ( weight == 1 ) {
        boxWeights.get(boxPositions.get( xLocation )).push(
            new CardBox( xLocation, boxWeights.get( boxPositions.get( xLocation )).peek().getyLocation()-40, currentBoxSpeed ));
      } else if ( weight == 2 ){
        boxWeights.get( boxPositions.get( xLocation )).push(
            new WoodBox( xLocation, boxWeights.get( boxPositions.get( xLocation )).peek().getyLocation()-40, currentBoxSpeed ));
      } else if ( weight == 3 ) {
        boxWeights.get( boxPositions.get( xLocation )).push(
            new MetalBox( xLocation, boxWeights.get( boxPositions.get( xLocation )).peek().getyLocation()-40, currentBoxSpeed ));
      }
      else if ( weight == 4){
        boxWeights.get(boxPositions.get(
            xLocation )).push( new StoneBox( xLocation, boxWeights.get( boxPositions.get( xLocation )).peek().getyLocation()-40, currentBoxSpeed ));
      }
      return true;
    }
    return false;
  }
  public void move(){
      yLocation += ySpeed;
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

  public Image getBoxImage() {
    return boxImage;
  }
}
