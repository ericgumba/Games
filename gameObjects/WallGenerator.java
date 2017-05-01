package gameObjects;

import java.awt.*; 
import java.util.ArrayList;

/** a
 * Created by ericgumba on 4/24/17.
 */
public class WallGenerator extends TankWorld {

  private ArrayList<IndestructibleWall> invincibleWalls = new ArrayList();
  private ArrayList<DestructibleWall> regularWalls = new ArrayList();

 /**
   *   WallGenerator constructor
   * @param k
   * @param keyEventType
   */ 
  WallGenerator() {}

 /**
   *  addWall constructor
   * @param x
   * @param y
   * @param c
   */ 
  public void addWall(int x, int y, char c) {
    if( c == '1'){
      invincibleWalls.add(new IndestructibleWall(x,y));
    } else if ( c == '2'){
      regularWalls.add(new DestructibleWall(x,y));
    }
  }
  
  
 /**
   *  draw constructor
   * @param g
   */   
  public void draw( Graphics g ) {
    for ( DestructibleWall wall : regularWalls ) {
      wall.update();
      wall.draw(g);
    }
    for ( IndestructibleWall wall : invincibleWalls ){
      wall.update();
      wall.draw( g );
    }
  }

 /**
   *  collision constructor
   * @param oX
   * @param oY
   * @param oW
   * @param oH
   */  
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
