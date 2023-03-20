package es.udc.redes.tutorial.tcp.server;
import java.io.IOException;
import java.net.*;

/** Multithread TCP echo server. */

public class TcpServer {

  public static void main(String argv[]) {
    if (argv.length != 1) {
      System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
      System.exit(-1);
    }
    ServerSocket socket=null;
    Socket s1;
    ServerThread serverThread=null;
    try {
      // Create a server socket
      socket= new ServerSocket(Integer.parseInt(argv[0]));
      // Set a timeout of 300 secs
      socket.setSoTimeout(300000);
      while (true) {
        // Wait for connections
        s1=socket.accept();
        // Create a ServerThread object, with the new connection as parameter
        serverThread=new ServerThread(s1);
        // Initiate thread using the start() method
        serverThread.start();
      }
    // Uncomment next catch clause after implementing the logic
     } catch (SocketTimeoutException e) {
      System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
     } finally{
	    //Close the socket
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
