package gameObjects;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

/**
 * Created by ericgumba on 4/24/17.
 */
public class WallGenerator extends TankWorld {

  private ArrayList<IndestructibleWall> invincibleWalls = new ArrayList();
  private ArrayList<DestructibleWall> regularWalls = new ArrayList();
  private int wallWidth = 38;


  WallGenerator() {


    // Draws the top and bottom walls.
    for(int i = 0; i < BACKGROUND_WIDTH / 32; i++){
      invincibleWalls.add( new IndestructibleWall( i * BACKGROUND_WIDTH / 32 , 10 ));
      invincibleWalls.add( new IndestructibleWall( i * BACKGROUND_WIDTH / 32, BACKGROUND_HEIGHT -20 ));
    }

    // Draws to left and right walls.
    for (int i = 0; i < BACKGROUND_HEIGHT / 32; i++) {
      invincibleWalls.add(new IndestructibleWall(BACKGROUND_WIDTH - 20, i * (BACKGROUND_HEIGHT / 32)));
      invincibleWalls.add( new IndestructibleWall( 10, i * ( BACKGROUND_HEIGHT / 32 )));
    }

    int checkPoint = 0;

    // draws the top players spawn point
    for ( int i = 0; i < 5 ; i++ ) {
      invincibleWalls.add( new IndestructibleWall( BACKGROUND_WIDTH /2, i * ( BACKGROUND_HEIGHT / 32 )));
      checkPoint = i * BACKGROUND_HEIGHT / 32;
    }
    for ( int i = 0; i < 6; i++ ){
      invincibleWalls.add(new IndestructibleWall(BACKGROUND_WIDTH /2 - i * wallWidth, checkPoint));
    }

    for ( int i = 0; i < 5; i++ ){
      invincibleWalls.add(new IndestructibleWall(BACKGROUND_WIDTH /2, BACKGROUND_HEIGHT - 38 * i ));
      checkPoint = BACKGROUND_HEIGHT - 38 * i;
    }

    for ( int i = 0; i < 5; i++ ){
      invincibleWalls.add(new IndestructibleWall(BACKGROUND_WIDTH /2 + i * wallWidth, checkPoint));
    }
    for ( int i = 0; i < 5; i++ ){
      invincibleWalls.add(new IndestructibleWall(38 + 38 * i, BACKGROUND_HEIGHT /2 - 38 * 2));
    }
    for ( int i = 0 ; i < 13; i++ ){
      regularWalls.add( new DestructibleWall( BACKGROUND_WIDTH / 2, 300 + i * 38 ));
      regularWalls.add( new DestructibleWall( BACKGROUND_WIDTH / 2 - 38, 300 + i * 38 ));
      regularWalls.add( new DestructibleWall( BACKGROUND_WIDTH / 2 + 38 + 38 * i, 300));
      regularWalls.add( new DestructibleWall( BACKGROUND_WIDTH / 2 + 38 - 38 * i, 300 + 13 * 38 ));
    }

  }

  public void draw(Graphics g, ImageObserver obs) {


    for ( DestructibleWall wall : regularWalls ) {
      wall.update();
      wall.draw(g);
    }
    for ( IndestructibleWall wall : invincibleWalls ){
      wall.update();
      wall.draw( g );
    }
  }

  public boolean collision( int oX, int oY, int oW, int oH ) {
    for ( DestructibleWall wall : regularWalls ) {
      if ( wall.collision( oX, oY, oW, oH )) {
        return true;
      }
    }
    for( IndestructibleWall wall : invincibleWalls ) {
      if ( wall.collision( oX, oY, oW, oH )) {
        return true;
      }
    }
    return false;
  }

}
