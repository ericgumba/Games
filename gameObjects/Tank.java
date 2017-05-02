package gameObjects;

import GameInterface.VehicleInterface;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

/** a
 * Created by Eric Gumba and Leo Wang on 4/22/17. d
 */
public class Tank extends TankWorld implements VehicleInterface {

  private BufferedImage tankImages;
  private ArrayList<Bullet> EnemyBullets, myBullets;
  private Image bulletImages;
  private int centerX, centerY;
  private int tankWidth, tankHeight;
  int x, y;
  private final int MAX_HP = 4;
  private int hp, score = 3;
  private int direction = 0, directionRate = 0, playerNumber, explodeStage = 0;
  private int xSpeed = 0, ySpeed = 0;
  private String healthPoints[] = {"HP: 4", "HP: 3", "HP: 2", "HP: 1", "HP: 0"};
  private String controlSet;

  Tank(String tankImages, ArrayList EnemyBullets, ArrayList myBullets, Image bulletImages, int playerNumber) {
    this.bulletImages = bulletImages;
    try {
      this.tankImages = imageGenerator.getBufferedImage(tankImages);
    } catch ( IOException e ) {
      System.out.println( e );
    }
    this.EnemyBullets = EnemyBullets;
    this.myBullets = myBullets;
    tankWidth = this.tankImages.getWidth() / 60;
    tankHeight = this.tankImages.getHeight();

    setXSpawnPoint( playerNumber );
    setYSpawnPoint( playerNumber );

    hp = MAX_HP;
    this.playerNumber = playerNumber;
    this.controlSet = playerNumber + "";
    centerX = x + tankWidth / 4;
    centerY = y + tankHeight / 4;
  }

  // new 4/29 for display score on screen
  public int getScore (){
      return this.score;
  }
  
  /**
   * Draws the tank at the position indicated by the move() method. If it dies then it draws both tanks
   * at their spawn point.
   * @param g
   * @param obs
   */
  public void draw(Graphics g, ImageObserver obs) {
    if (hp > 0) {
      BufferedImage currentImage = tankImages.getSubimage(tankWidth * (direction / 6), 0, tankWidth, tankHeight);
      g.drawImage(currentImage, x, y, obs);
      g.setColor(Color.YELLOW);
      g.drawString(healthPoints[4 - hp], x + 5, y - 2);
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

        player[otherPlayer].score -= 1;
        player[playerNumber].setXSpawnPoint( otherPlayer );
        player[playerNumber].setYSpawnPoint( otherPlayer );
        death.play();
      }
    }
  }

  /**
   * If the tank position <= position of object and position of object >= tank position then the tank has collided
   * with another object.
   * @param xPositionOfObject
   * @param yPositionOfObject
   * @param widthOfObject
   * @param heightOfObject
   * @return
   */
  private boolean collision(int xPositionOfObject, int yPositionOfObject, int widthOfObject, int heightOfObject) {

    if ( this.y < yPositionOfObject + heightOfObject
       && this.y + tankHeight > yPositionOfObject )
        if ( this.x < xPositionOfObject + widthOfObject
            && this.x + tankWidth > xPositionOfObject ) {
            return true;
    }
    return false;
  }

 /**
   * move method.
   */
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
    if (( y + ySpeed < BACKGROUND_HEIGHT - 70 ) && ( y + ySpeed > 0 )   //88
        && ( !( player[ playerNumber ].collision( x, y + ySpeed, tankWidth, tankHeight )))
        && ( !( wallGenerator.collision( x, y + ySpeed, tankWidth, tankHeight )))
        ) {
      y += ySpeed;
    }
      for ( Bullet bullet: EnemyBullets ){
        if ( bullet.collision( x + 20, y, tankWidth - 20, tankHeight )) {
          if ( hp >= 1 ){
            hp -= 1;
          }
        }
      }
  }

  /**
   * setXSpawnPoint private method
   * Sets spawn point depending on the player number
   * @param playerNumber
   */
  private void setXSpawnPoint(int playerNumber){
    if ( playerNumber == 1 ) {
      x = playerNumber * 60 + BACKGROUND_WIDTH / 3;
    } else {
      x = playerNumber + 260 + BACKGROUND_WIDTH / 3; //400->300->220
    }
  }

  /**
   * setYSpawnPoint private method
   * Sets spawn point depending on the player number
   * @param playerNumber
   */  
  private void setYSpawnPoint(int playerNumber){
    if ( playerNumber == 2 ) {
      y = 520 + BACKGROUND_HEIGHT / 2;
    }
    else {
      y = -580 + BACKGROUND_HEIGHT / 2;
    }
  }

  /**
   *  update method
   *  controlset is either 1 or 2
   *  if the controlset is 1 then it moves player 1's tank
   *  if it's 2 then it moves player 2's tank.
   * @param obj
   * @param event
   */
  public void update( Observable obj, Object event ) {
    TankWorldEvents gameE = ( TankWorldEvents ) event;
    if ( gameE.eventType <= 1 ) {
      String action = controls.get(gameE.event);
      //System.out.println("controls action "+action);
      if(null==action) {
      } else {
          if ( action.equals( "left" + controlSet )) {
              directionRate = 6 * gameE.eventType;
          } else if ( action.equals( "right" + controlSet )) {
              directionRate = -6 * gameE.eventType;
          } else if ( action.equals( "up" + controlSet )) {
              ySpeed = ( int ) ( -1 * 10 * Math.sin( Math.toRadians( direction ))) * gameE.eventType;
              xSpeed = ( int ) ( 10 * Math.cos( Math.toRadians( direction ))) * gameE.eventType;
          } else if ( action.equals( "down" + controlSet )) {
              ySpeed = ( int ) ( 10 * Math.sin( Math.toRadians( direction ))) * gameE.eventType;
              xSpeed = ( int ) ( -1 * 10 * Math.cos( Math.toRadians( direction ))) * gameE.eventType;
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
}
