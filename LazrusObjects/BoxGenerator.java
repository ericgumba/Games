package LazrusObjects;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.List;

/**
 * Created by ericgumba on 4/27/17.
 */
public class BoxGenerator extends LazarusWorld {


  ArrayList<Box> box = new ArrayList();
  public BoxGenerator() {

    int wallHeight = 40;

    for (int i = 0; i < 16; i++) {
      boxWeights.get(i).push( new Wall ( 40*i, GAMEBOARD_HEIGHT-63 ));
      boxWeights.get( i ).push( new Wall (40*i, GAMEBOARD_HEIGHT - 103 ));
    }
    for(int i = 0; i < 2; i++){
      boxWeights.get(i).push( new Wall(wallHeight*i, GAMEBOARD_HEIGHT- ( 63 + wallHeight * 2 )));
      boxWeights.get(i).push( new Wall( wallHeight*i, GAMEBOARD_HEIGHT - ( 63 + wallHeight * 3 )));
      boxWeights.get(i).push( new Wall(wallHeight*i, GAMEBOARD_HEIGHT - ( 63 + wallHeight * 4 )));
      boxWeights.get(i).push( new Wall( wallHeight*i, GAMEBOARD_HEIGHT - ( 63 + wallHeight * 5 )));
      boxWeights.get(i+14).push( new Wall ( wallHeight * ( 14+i ), GAMEBOARD_HEIGHT - ( 63 + wallHeight * 2 )));
      boxWeights.get(14+i).push( new Wall ( wallHeight * ( 14+i ), GAMEBOARD_HEIGHT - ( 63 + wallHeight * 3 )));
      boxWeights.get(14+i).push( new Wall ( wallHeight * ( 14+i ), GAMEBOARD_HEIGHT - ( 63 + wallHeight * 4 )));
      boxWeights.get(14+i).push( new Wall ( wallHeight * ( 14+i ), GAMEBOARD_HEIGHT - ( 63 + wallHeight * 5 )));

    }
    box.add( new Button ( 0, GAMEBOARD_HEIGHT - ( 63 + wallHeight * 6 ) ));
    box.add( new Button( 40*15, GAMEBOARD_HEIGHT - ( 63 + wallHeight * 6 )));


    /**
     * The following is only used as a demo. To be redacted at a later date.
     */
    boxWeights.get(10).push(new Wall ( wallHeight * 10, GAMEBOARD_HEIGHT - (63 + wallHeight * 2 )));
    boxWeights.get(11).push(new Wall ( wallHeight * 11, GAMEBOARD_HEIGHT - (63 + wallHeight * 2 )));
    boxWeights.get(12).push(new Wall ( wallHeight * 12, GAMEBOARD_HEIGHT - (63 + wallHeight * 2 )));
    boxWeights.get(12).push(new Wall ( wallHeight * 12, GAMEBOARD_HEIGHT - (63 + wallHeight * 3 )));


  }
  public void draw ( Graphics g ){

      for (Box b : box) {
        b.draw(g);
      }

      for ( int i = 0 ; i < boxWeights.size(); i++ ){
        Iterator<Box> iter = boxWeights.get(i).iterator();
        while (iter.hasNext()){
          iter.next().draw(g);
        }

      }



  }
}
