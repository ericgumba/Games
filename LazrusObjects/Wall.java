package LazrusObjects;

/**
 * Created by ericgumba on 4/28/17.
 */
public class Wall extends Box {

  public Wall( int x, int y){
    boxImage = imgGen.getImage("Lazarus/Wall.png");
    xLocation = x;
    yLocation = y;
  }
  @Override
  public int weight() {
    return 99;
  }
}
