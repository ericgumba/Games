package gameObjects;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/** a
 * Created by eric gumba and Leo Wang on 4/23/17. d
 */

public class NetworkReader extends TankWorld implements Runnable {

  private DataInputStream reader;
  private TankWorldEvents tnkWorldEvents;
 
  /** 
   * NetworkReader constructor
   * @param socket
   * @param tnkWorldEvents
   */       
  public NetworkReader (Socket socket, TankWorldEvents tnkWorldEvents) {  
      try {
          reader = new DataInputStream( socket.getInputStream());
          this.tnkWorldEvents = tnkWorldEvents;
      } catch (IOException ex) {
          Logger.getLogger(NetworkReader.class.getName()).log(Level.SEVERE, null, ex);
      }
  }  
 
  /** 
   * run method
   * 
   */    
  @Override
  public void run() {
      
      while (true) {
          try {
              int event = reader.readInt();
              int eventType = reader.readInt();
              tnkWorldEvents.setValue(event, eventType);
              //System.out.println ("reader : "+event+" "+eventType);
          } catch (IOException ex) {
              Logger.getLogger(NetworkReader.class.getName()).log(Level.SEVERE, null, ex);
          }   
      }
  }
}  


