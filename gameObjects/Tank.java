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
  private int centerX, centerY;
  private int tankWidth, tankHeight;
  int x, y;
  private final int MAX_HP = 4;
  private int hp;
  private int direction = 0, directionRate = 0, playerNumber, explodeStage = 0;
  private int xSpeed = 0, ySpeed = 0;
  private String healthPoints[] = {"HP: 4", "HP: 3", "HP: 2", "HP: 1", "HP: 0"};
  private String controlSet;
  Tank(String tankImages, ArrayList EnemyBullets, ArrayList myBullets, Image bulletImages, int playerNumber) {

    this.bulletImages = bulletImages;
    try {
      this.tankImages = imageGenerator.getBufferedImage(tankImages);
    } catch ( IOException e ) {
      System.out.println(e);
    }
    this.EnemyBullets = EnemyBullets;
    this.myBullets = myBullets;
    tankWidth = this.tankImages.getWidth() / 60;
    tankHeight = this.tankImages.getHeight();

    setXSpawnPoint(playerNumber);
    setYSpawnPoint(playerNumber);

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

        setXSpawnPoint( playerNumber );
        setYSpawnPoint( playerNumber );

        int otherPlayer = 2;

        if ( playerNumber == 1 ){
          otherPlayer = 2;
        } else {
          otherPlayer = 1;
        }
        player[playerNumber].setXSpawnPoint( otherPlayer );
        player[playerNumber].setYSpawnPoint( otherPlayer );
//        explosionSound_1.play();
      }
    }
  }

  private boolean collision(int xPositionOfObject, int yPositionOfObject, int widthOfObject, int heightOfObject) {

    if ( this.y < yPositionOfObject + heightOfObject
       && this.y + tankHeight > yPositionOfObject )
        if ( this.x < xPositionOfObject + widthOfObject
            && this.x + tankWidth > xPositionOfObject ) {
            return true;

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
    if (( x + xSpeed < BACKGROUND_WIDTH - 70 ) && ( x + xSpeed > 0 )
        && ( !(player[playerNumber].collision(x + xSpeed, y, tankWidth, tankHeight )))
        && ( !( wallGenerator.collision( x + xSpeed, y, tankWidth, tankHeight )))
        ) {
      x += xSpeed;
    }
    if (( y + ySpeed < BACKGROUND_HEIGHT - 88 ) && ( y + ySpeed > 0 )
        && ( !( player[ playerNumber ].collision( x, y + ySpeed, tankWidth, tankHeight )))
        && ( !( wallGenerator.collision( x, y + ySpeed, tankWidth, tankHeight )))
        ) {
      y += ySpeed;
    }

    for ( int i = 0; i < EnemyBullets.size(); i++ ) {
      if ( EnemyBullets.get(i).collision(x + 20, y, tankWidth - 20, tankHeight )) {
        if ( hp >= 1 ) {
          hp -=  1;
//          explosionSound_2.play();
        }
      }
    }
  }

  private void setXSpawnPoint(int playerNumber){


    if ( playerNumber == 1 ) {
      x = playerNumber * 60 + BACKGROUND_WIDTH / 3;
    } else {
      x = playerNumber + 400 + BACKGROUND_WIDTH / 3;
    }

  }
  private void setYSpawnPoint(int playerNumber){

    if ( playerNumber == 2 ) {
      y = 460 + BACKGROUND_HEIGHT / 2;
    }
    else {
      y = -500 + BACKGROUND_HEIGHT / 2;
    }
  }
  public void update( Observable obj, Object event ) {
    TankWorldEvents gameE = ( TankWorldEvents ) event;

    if ( gameE.eventType <= 1 ) {
      KeyEvent e = ( KeyEvent ) gameE.event;
      String action = controls.get(e.getKeyCode());
      if ( action.equals( "left" + controlSet )) {
        directionRate = 6 * gameE.eventType;
      } else if ( action.equals( "right" + controlSet )) {
        directionRate = -6 * gameE.eventType;
      } else if ( action.equals( "up" + controlSet )) {
        ySpeed = ( int ) ( -1 * 5 * Math.sin( Math.toRadians( direction ))) * gameE.eventType;
        xSpeed = ( int ) ( 5 * Math.cos( Math.toRadians( direction ))) * gameE.eventType;
      } else if ( action.equals( "down" + controlSet )) {
        ySpeed = ( int ) ( 5 * Math.sin( Math.toRadians( direction ))) * gameE.eventType;
        xSpeed = ( int ) ( -1 * 5 * Math.cos( Math.toRadians( direction ))) * gameE.eventType;
      } else if ( action.equals( "shoot" + controlSet )) {
        if ( gameE.eventType == 0 ) {
          int bulletXSpeed = ( int) ( 15 * Math.cos(Math.toRadians( direction )));
          int bulletYSpeed = ( int ) ( -15 * Math.sin( Math.toRadians( direction )));
          centerX = x + tankWidth / 4 + bulletXSpeed * 2;
          centerY = y + tankHeight / 4 + bulletYSpeed * 2;
          myBullets.add( new Bullet( bulletImages, centerX, centerY, bulletXSpeed, bulletYSpeed ));
        }

      }
    }
  }


}
