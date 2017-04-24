package gameObjects;

import GameInterface.VehicleInterface;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by ericgumba on 4/22/17.
 */
public class Tank extends TankWorld implements VehicleInterface {


  private BufferedImage tankImages;
  private ArrayList<Bullet> EnemyBullets, myBullets;
  private Image bulletImages;
  int centerX, centerY, bulletType = 0, bulletXSpeed = 0, bulletYSpeed = 0;
  int tankWidth, tankHeight, x, y;
  final int MAX_HP = 4;
  int hp, score = 0;
  int speed = 10, direction = 0, directionRate = 0, playerNumber, explodeStage = 5;
  ImageObserver obs;
  int xSpeed = 0, ySpeed = 0;
  private String healthPoints[] = {"HP: 4", "HP: 3", "HP: 2", "HP: 1", "HP: 0"};
  private String controlSet;

  Tank(String tankImages, ArrayList EnemyBullets, ArrayList myBullets, Image bulletImages, int playerNumber) {
    this.bulletImages = bulletImages;
    try {
      this.tankImages = imageGenerator.getBufferedImage(tankImages);
    } catch (IOException e) {
      System.out.println(e);
    }
    this.EnemyBullets = EnemyBullets;
    this.myBullets = myBullets;
    tankWidth = this.tankImages.getWidth() / 60;
    tankHeight = this.tankImages.getHeight();
    x = playerNumber * borderX / 3;
    y = borderY / 2;
    hp = MAX_HP;
    this.playerNumber = playerNumber;
    this.controlSet = playerNumber + "";
    centerX = x + tankWidth / 4;
    centerY = y + tankHeight / 4;
  }

  public void draw(Graphics g, ImageObserver obs) {


    if (hp > 0) {
      BufferedImage currentImage = tankImages.getSubimage(tankWidth * (direction / 6), 0, tankWidth, tankHeight);
      g.drawImage(currentImage, x, y, obs);
      g.setColor(Color.YELLOW);
      g.drawString(healthPoints[4 - hp], x + 5, y - 10); // CP
    } else if (hp <= 0) {
      g.drawImage(explosionFrames[explodeStage++], x, y, obs);
      if (explodeStage == 7) {
        explodeStage = 0;
        hp = MAX_HP;
        x = playerNumber * borderX / 3;
        y = playerNumber * borderY / 3;
        player[playerNumber].score += 5;
        player[playerNumber].x = (player[playerNumber].playerNumber) * borderX / 3;
        player[playerNumber].y = (player[playerNumber].playerNumber) * borderY / 3;
//        explosionSound_1.play();
      }
    }
  }

  public boolean collision(int x, int y, int w, int h) {
    x += 5;
    y += 5;
    w -= 10;
    h -= 15;
    if ((y + h > this.y) && (y < this.y + tankHeight)) {
      if ((x + w > this.x) && (x < this.x + tankWidth)) {
        return true;
      }
    }
    return false;
  }

  public void move() {
    direction += directionRate;
    if (direction == -6) {
      direction = 354;

    } else if (direction == 360) {
      direction = 0;
    }
    if ((x + xSpeed < borderX - 70) && (x + xSpeed > 0)
        && (!(player[playerNumber].collision(x + xSpeed, y, tankWidth, tankHeight)))
        && (!(wallGenerator.collision(x + xSpeed, y, tankWidth, tankHeight)))
        ) {
      x += xSpeed;
    }
    if ((y + ySpeed < borderY - 88) && (y + ySpeed > 0)
        && (!(player[playerNumber].collision(x, y + ySpeed, tankWidth, tankHeight)))
        && (!(wallGenerator.collision(x, y + ySpeed, tankWidth, tankHeight)))
        ) {
      y += ySpeed;
    }

    for (int i = 0; i < EnemyBullets.size(); i++) {
      if (EnemyBullets.get(i).collision(x + 20, y, tankWidth - 20, tankHeight)) {
        if (hp >= 1) {
          hp -= (player[playerNumber].bulletType + 1);
//          explosionSound_2.play();
        }
      }
    }

  }

  public void update(Observable obj, Object event) {
    TankWorldEvents gameE = (TankWorldEvents) event;

    if (gameE.eventType <= 1) {
      KeyEvent e = (KeyEvent) gameE.event;
      String action = controls.get(e.getKeyCode());
      if (action.equals("left" + controlSet)) {
        directionRate = 6 * gameE.eventType;
      } else if (action.equals("right" + controlSet)) {
        directionRate = -6 * gameE.eventType;
      } else if (action.equals("up" + controlSet)) {
        ySpeed = (int) (-1 * speed * Math.sin(Math.toRadians(direction))) * gameE.eventType;
        xSpeed = (int) (speed * Math.cos(Math.toRadians(direction))) * gameE.eventType;
      } else if (action.equals("down" + controlSet)) {
        ySpeed = (int) (speed * Math.sin(Math.toRadians(direction))) * gameE.eventType;
        xSpeed = (int) (-1 * speed * Math.cos(Math.toRadians(direction))) * gameE.eventType;
      } else if (action.equals("shoot" + controlSet)) {
        if (gameE.eventType == 0) {
          bulletXSpeed = (int) (15 * Math.cos(Math.toRadians(direction)));
          bulletYSpeed = (int) (-15 * Math.sin(Math.toRadians(direction)));
          centerX = x + tankWidth / 4 + bulletXSpeed * 2;
          centerY = y + tankHeight / 4 + bulletYSpeed * 2;
          myBullets.add(new Bullet(bulletImages, centerX, centerY, bulletXSpeed, bulletYSpeed));
        }

      }
    }
  }


}
