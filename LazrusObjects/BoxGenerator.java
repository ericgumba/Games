package LazrusObjects;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by ericgumba on 4/27/17.
 */
public class BoxGenerator extends LazarusWorld {
  Stack<Box> boxes;
  ArrayList<CardBox> box = new ArrayList();
  public BoxGenerator() {

    for (int i = 0; i < 16; i++) {
      box.add(new CardBox(40*i, GAMEBOARD_HEIGHT-63));
    }


  }
  public void draw ( Graphics g ){
    for(CardBox boxes : box){
      boxes.draw( g );
    }
  }
}
