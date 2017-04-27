package LazarusModel;

import LazarusView.LazarusWorld;

import java.awt.*;

/**
 * Created by ericgumba on 4/26/17.
 */
public abstract class Crate extends LazarusWorld {
  int weight;
  Image crateImage;

  public abstract int weight();
}
