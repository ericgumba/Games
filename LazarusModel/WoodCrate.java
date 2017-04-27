package LazarusModel;
import LazarusView.LazarusWorld;

import java.awt.*;

/**
 * Created by ericgumba on 4/26/17.
 */
public class WoodCrate extends Crate {

  public WoodCrate(Image crate, int x, int y){
    crateImage = crate;
  }

  public int weight(){
    return 1;
  }
}
