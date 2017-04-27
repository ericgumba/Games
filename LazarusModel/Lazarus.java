package LazarusModel;

import LazarusView.LazarusWorld;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Created by ericgumba on 4/26/17.
 */
public class Lazarus extends LazarusWorld {
  int xLocation, yLocation;
  Image imageOfLazarus;
  Lazarus(){

  }
  public void move(){

  }
  public void draw(ImageObserver obs, Graphics g){
    g.drawImage(imageOfLazarus, xLocation, yLocation, obs);
  }
  public void update(){
    
  }
}
