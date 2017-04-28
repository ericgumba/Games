package LazrusObjects;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by ericgumba on 4/27/17.
 */
public class BoxGenerator extends LazarusWorld {
  Stack<Box> boxes;
  ArrayList<Box> box = new ArrayList();
  public BoxGenerator() {

    int wallHeight = 40;
    for (int i = 0; i < 16; i++) {
      box.add( new Wall ( 40*i, GAMEBOARD_HEIGHT-63 ));
      box.add( new Wall (40*i, GAMEBOARD_HEIGHT - 103 ));
    }
    for(int i = 0; i < 2; i++){
      box.add( new Wall(wallHeight*i, GAMEBOARD_HEIGHT- ( 63 + wallHeight * 2 )));
      box.add( new Wall( wallHeight*i, GAMEBOARD_HEIGHT - ( 63 + wallHeight * 3 )));
      box.add( new Wall(wallHeight*i, GAMEBOARD_HEIGHT - ( 63 + wallHeight * 4 )));
      box.add( new Wall( wallHeight*i, GAMEBOARD_HEIGHT - ( 63 + wallHeight * 5 )));
      box.add( new Wall ( wallHeight * ( 14+i ), GAMEBOARD_HEIGHT - ( 63 + wallHeight * 2 )));
      box.add( new Wall ( wallHeight * ( 14+i ), GAMEBOARD_HEIGHT - ( 63 + wallHeight * 3 )));
      box.add( new Wall ( wallHeight * ( 14+i ), GAMEBOARD_HEIGHT - ( 63 + wallHeight * 4 )));
      box.add( new Wall ( wallHeight * ( 14+i ), GAMEBOARD_HEIGHT - ( 63 + wallHeight * 5 )));
    }
    box.add( new Button ( 0, GAMEBOARD_HEIGHT - ( 63 + wallHeight * 6 ) ));
    box.add( new Button( 40*15, GAMEBOARD_HEIGHT - ( 63 + wallHeight * 6 )));


  }
  public void draw ( Graphics g ){
    for(Box boxes : box){
      boxes.draw( g );
    }
  }
}
