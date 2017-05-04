package LazrusObjects;

/**
 * Created by ericgumba on 5/1/17.
 */
public class StoneBox extends Box {

  public StoneBox(int x, int y, int boxSpeed){
    weight = 4;
    boxImage = imgGen.getImage("Lazarus/StoneBox.png");
    xLocation = x;
    yLocation = y;
    ySpeed = boxSpeed;
  }

  @Override
  public int weight() {
    return 4;
  }
}
