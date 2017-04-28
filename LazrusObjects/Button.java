package LazrusObjects;

/**
 * Created by ericgumba on 4/28/17.
 */
public class Button extends Box{
  public Button(int x, int y){
    boxImage = imgGen.getImage( "Lazarus/Button.png" );
    xLocation = x;
    yLocation = y;
  }

  @Override
  public int weight() {
    return -1;
  }
}
