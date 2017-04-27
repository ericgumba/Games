package LazrusObjects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by ericgumba on 4/26/17.
 */
public class ImageGenerator extends LazarusWorld {

  public Image getImage(String name ) {
    try {
      URL url = LazarusWorld.class.getResource( name );
      Image img = getToolkit().getImage( url );
      MediaTracker tracker = new MediaTracker(this);
      tracker.addImage( img, 0 );
      tracker.waitForID(0);
      return img;
    } catch ( Exception e ) {
      System.out.println( e + "the image can't be added: " + name );
      return null;
    }
  }

  public BufferedImage getBufferedImage(String name ) throws IOException {
    URL url = LazarusWorld.class.getResource( name );
    BufferedImage img = ImageIO.read( url );
    try {
      MediaTracker tracker = new MediaTracker(this);
      tracker.addImage( img, 0 );
      tracker.waitForID( 0 );
    } catch ( Exception e ) {
      System.out.println( e + "the image can't be added" + name );
    }
    return img;
  }
}
