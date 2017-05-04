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


    // problem, increase height by 2 or something.

    boxTypes.put( 1, new CardBox( 0, GAMEBOARD_HEIGHT-63, currentBoxSpeed ));
    boxTypes.put( 2, new WoodBox( 0, GAMEBOARD_HEIGHT-63, currentBoxSpeed ));
    boxTypes.put( 3, new MetalBox( 0, GAMEBOARD_HEIGHT-63, currentBoxSpeed ));
    boxTypes.put( 4, new StoneBox( 0, GAMEBOARD_HEIGHT-63, currentBoxSpeed ));


    int wallHeight = 40;

    for ( int i = 0; i < 16; i++ ) {
      boxWeights.get( i ).push( new Wall ( 40*i, GAMEBOARD_HEIGHT-63 ));
      boxWeights.get( i ).push( new Wall (40*i, GAMEBOARD_HEIGHT - 103 ));
    }
    for(int i = 0; i < 2; i++){
      for( int j = 0; j < 3+currentLevel; j++ ){
        boxWeights.get( i ).push(
            new Wall(
                wallHeight*i, GAMEBOARD_HEIGHT - (63 + wallHeight * ( 2+j ))));
        boxWeights.get(14+i).push( new Wall(
            wallHeight * ( 14+i ), GAMEBOARD_HEIGHT - ( 63 + wallHeight * ( 2+j ))));
      }
    }
    box.add( new Button ( 0, GAMEBOARD_HEIGHT - ( 63 + wallHeight * ( 5+currentLevel ))));
    box.add( new Button( 40*15, GAMEBOARD_HEIGHT - ( 63 + wallHeight * ( 5+currentLevel ))));


  }

  public void addBox(int x,int y, int boxNum, int boxSpeed){
    if ( boxNum == 1 ) {
      box.add( new CardBox( x, y, boxSpeed ));
    } else if ( boxNum == 2 ){
      box.add( new WoodBox( x, y, boxSpeed ));
    } else if ( boxNum == 3 ){
      box.add( new MetalBox( x, y, boxSpeed ));
    } else if ( boxNum == 4){
      box.add( new StoneBox( x, y, boxSpeed ));
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
