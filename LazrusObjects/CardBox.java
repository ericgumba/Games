package LazrusObjects;

import java.awt.*;

/**
 * Created by ericgumba on 4/27/17.
 */
public class CardBox  extends Box{

  // useful information about the boxes
  final int BOX_WIDTH = 40;
  final int BOX_HEIGHT = 40;


  public CardBox( int x, int y ){
    boxImage = imgGen.getImage("Lazarus/CardBox.png");
    xLocation = x;
    yLocation = y;
  }
  public int weight(){
    return 1;
  }
}
