package gameObjects;
import gameObjects.Tanks.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ericgumba on 4/18/17.
 */
public class TankWorld extends JApplet implements Runnable {


  static HashMap<Integer, String> controls = new HashMap<>();
  static int borderX = 1475, borderY = 115;
  private Thread gameThread;
  private Background gameBoard;
  private ImageObserver observer;
  BufferedImage imageOne, imageTwo;
  static Tank tankOne, tankTwo;
  static Image[] health;
  static ArrayList<Bullets> tankOneBullets, tankTwoBullets;
  //



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
    controls.put(KeyEvent.VK_LEFT, "left");
    controls.put(KeyEvent.VK_UP, "up");
    controls.put(KeyEvent.VK_DOWN, "down");
    controls.put(KeyEvent.VK_RIGHT, "right");
    


  }

  public void start(){
    gameThread = new Thread(this);
    gameThread.setPriority(Thread.MIN_PRIORITY);
    gameThread.start();
  }
}
