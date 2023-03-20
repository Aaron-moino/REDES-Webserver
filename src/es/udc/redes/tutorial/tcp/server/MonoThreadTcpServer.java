package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        ServerSocket socket =null;
        Socket s1;
        try {
            // Create a server socket
            socket=new ServerSocket(Integer.parseInt(argv[0]));
            // Set a timeout of 300 secs
            socket.setSoTimeout(300000);
            while (true) {
                // Wait for connections
                s1=socket.accept();
                // Set the input channel
                BufferedReader sInput = new BufferedReader(new InputStreamReader(
                        s1.getInputStream()));
                // Set the output channel
                PrintWriter sOutput = new PrintWriter(s1.getOutputStream(), true);
                // Receive the client message
                String received = sInput.readLine();
                System.out.println("SERVER: Received " + received
                        + " from " + s1.getInetAddress().toString()
                        + ":" + s1.getPort());
                // Send response to the client
                sOutput.println(received);
                System.out.println("SERVER: Send " + received
                        + " from " + s1.getInetAddress().toString()
                        + ":" + s1.getPort());
                // Close the streams
                sOutput.close();
                sInput.close();
            }
        // Uncomment next catch clause after implementing the logic            
        } catch (SocketTimeoutException e) {
           System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
	        //Close the socket
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
