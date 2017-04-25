package src.network;

//import examples.network.ObservableKeyEntry;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  @ SFSU CSC413 assignment 4 4/25/2017
 */

//public class NetworkServer implements Observer {
public class NetworkPanel {
    
    private BufferedReader reader;
    private BufferedWriter writer;
    //private ObservableKeyEntry keyListener;
    //private KeyBroadcaster broadcaster;    
   
    //keyListener = new ObservableKeyEntry();
    //this.addKeyListener( keyListener );
    
    public NetworkPanel (Integer port) {
            super();
            try {
                ServerSocket server = new ServerSocket( port );
                Socket connection = server.accept();
                System.out.println( "Socket accepted by server" ); 

                reader = new BufferedReader(
                            new InputStreamReader( connection.getInputStream() )
                        );
                writer = new BufferedWriter(
                            new OutputStreamWriter( connection.getOutputStream() )
                        );
                System.out.println("reader "+reader+" writer "+writer);
                //keyListener = new ObservableKeyEntry();

            } catch( IOException exception ) {
                System.err.println( "Failed to establish connection" );
            }
    }
    
        public NetworkPanel (String ipAddress, Integer port) {
            super();
            try {
                Socket connection = new Socket(ipAddress, port);
                System.out.println("socket initiated by client");
                reader = new BufferedReader(
                            new InputStreamReader( connection.getInputStream() )
                        );
                writer = new BufferedWriter(
                            new OutputStreamWriter( connection.getOutputStream() )
                        );
                System.out.println("reader "+reader+" writer "+writer);
                //keyListener = new ObservableKeyEntry();
                
            } catch( IOException exception ) {
                System.err.println( "Failed to establish connection" );
            }
    }
    
    /*
    @Override
    public void update( Observable o, Object arg ) {
            ObservableKeyEntry observable = (ObservableKeyEntry) o;

        try {
            this.writer.write( observable.getLastKey() );
            this.writer.flush();
        } catch( IOException exception ) {
            System.err.println( "Failed to send key." );
        }
    }
    */
        
    public static void main( String[] args ) {
        
       Integer argumentLength = args.length;
    
       if (argumentLength < 0) {
            System.out.println("Argument Format Error"); 
        } else if (argumentLength == 0) {
            System.out.println("Game is running in Single machine mode");
            // gameMode = 0;
        } else if (argumentLength == 1) {  
          
               String number = args[0];
               Integer port = Integer.valueOf(number);
               System.out.println("Game is running in Server mode, port ="+port);
               NetworkPanel nS = new NetworkPanel(port);
               // gameMode = 1;
               //String answer = nS.reader.toString();
               //System.out.println(" received "+answer);
           
        } else if (argumentLength == 2) {    
            String ipAddress = args[0];
            String number = args[1];
            Integer port = Integer.valueOf(number);
            System.out.println("Game is running in Client mode: IPaddress "+ipAddress+" port "+port);
            NetworkPanel nS = new NetworkPanel(ipAddress, port);
                              //out.println(new Date().toString());
            //String test = "testing";
            //nS.writer.write(test);
            
            //keyListener = new ObservableKeyEntry();
            //this.addKeyListener( keyListener );
            //keyListener.addObserver( nS );
            //gameMode = 2;   
        } 
    

        /*
        this.addKeyListener( keyListener );
        keyListener.addObserver( broadcaster );
        */
        
    } // main
}
