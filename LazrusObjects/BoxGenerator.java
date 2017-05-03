package LazrusObjects;

import java.awt.*;
import java.awt.image.ImageObserver;
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


  }

  public void addBox(int x,int y, int boxNum){

    if ( boxNum == 1 ) {
      box.add( new CardBox( x, y ));
    } else if ( boxNum == 2 ){
      box.add( new StoneBox( x, y ));
    } else if ( boxNum == 3 ){
      box.add( new WoodBox( x, y ));
    } else if ( boxNum == 4){
      box.add( new MetalBox( x, y ));
    }

  }

  public ArrayList<Box> getBox() {
    return box;
  }

  public void draw (Graphics g, ImageObserver obs){
      for (Box b : box) {
        b.draw(g, obs);
        b.move();
      }
      for ( int i = 0 ; i < boxWeights.size(); i++ ){
        Iterator<Box> iter = boxWeights.get(i).iterator();
        while (iter.hasNext()){
          iter.next().draw(g, obs);
        }
      }
  }
}
