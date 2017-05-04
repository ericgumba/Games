package LazrusObjects;

/**
 * Created by ericgumba on 5/1/17.
 */
public class WoodBox extends Box {

  public  WoodBox( int x, int y, int boxSpeed ){
    weight = 2;
    xLocation = x;
    yLocation = y;
    boxImage = imgGen.getImage( "Lazarus/WoodBox.png" );
    ySpeed = boxSpeed;
  }
  @Override
  public int weight() {
    return 2;
  }
}
