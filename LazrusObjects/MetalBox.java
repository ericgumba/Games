package LazrusObjects;

/**
 * Created by ericgumba on 5/1/17.
 */
public class MetalBox extends Box {

  public MetalBox(int x, int y){
    xLocation = x;
    yLocation = y;
    boxImage = imgGen.getImage( "Lazarus/MetalBox.png" );
    weight = 3;
  }

  @Override
  public int weight() {
    return 3;
  }
}
