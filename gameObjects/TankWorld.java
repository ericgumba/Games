package gameObjects;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Created by ericgumba on 4/20/17.
 */
public class TankWorld extends JApplet {
    Background gameBoard;
    ImageObserver observer;

  public void initializeGame(){
    gameBoard = new Background();
    setBackground(Color.BLACK);
    this.setFocusable(true);
    observer = this;
  }


  public void paint(Graphics graphics){
    graphics.drawOval(0,0,100,100);

  }

  public Image getImage(String resource){
    try{
      Image image = getToolkit()
                    .getImage(
                    TankWorld.class
                    .getResource(resource));
      return image;

    } catch (Exception e) {
      System.out.println(" unable to get image " + resource);
      return null;
    }
  }

}
