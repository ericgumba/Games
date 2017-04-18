package gameObjects;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;

/**
 * Created by ericgumba on 4/18/17.
 */
public class TankWorld extends JApplet implements Runnable {


  static HashMap<Integer, String> controls = new HashMap<>();
  private Thread gameThread;
  private Background gameBoard;
  private ImageObserver observer;
  BufferedImage imageOne, imageTwo;



  @Override
  public void run() {
    Thread gameThread = Thread.currentThread();
    while (this.gameThread == gameThread) {
      repaint();
      try {
        this.gameThread.sleep(20);
      } catch (InterruptedException e) {
        break;
      }
    }
  }

  public void initializeGame() {
    gameBoard = new Background();

  }

  public void start(){
    gameThread = new Thread(this);
    gameThread.setPriority(Thread.MIN_PRIORITY);
    gameThread.start();
  }
}