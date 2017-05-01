package gameObjects;

import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/** a
 * Created by eric gumba and Leo Wang on 4/23/17. 
 */

public class NetworkWriter extends TankWorld implements Observer {

  private DataOutputStream writer;

 /** 
   * NetworkWriter constructor
   * @param socket
   */       
  public NetworkWriter (Socket socket) {  
      try {
           writer = new DataOutputStream( socket.getOutputStream());
      } catch (IOException ex) {
          Logger.getLogger(NetworkWriter.class.getName()).log(Level.SEVERE, null, ex);
      }
  }

 /** 
   * update method
   * @param obj
   * @param event
   */    
  public void update( Observable obj, Object event ) {
          NetworkEvents gameE = ( NetworkEvents ) event;
          if ( gameE.eventType <= 1 ) {
              String action = controls.get(gameE.event);
              try {
                    if(null==action) {
                    } else {
                            writer.writeInt(gameE.event);
                            writer.writeInt(gameE.eventType);
                            //System.out.println("writer "+gameE.event+" "+action+" "+gameE.eventType);
                    }            
                  } catch (IOException ex) {
                     Logger.getLogger(NetworkWriter.class.getName()).log(Level.SEVERE, null, ex);
                  }
          }    
   }
  
  
}         

