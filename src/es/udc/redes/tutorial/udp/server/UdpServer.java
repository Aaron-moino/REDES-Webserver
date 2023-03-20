package es.udc.redes.tutorial.udp.server;

import java.net.*;

/**
 * AL server se la pela si recibe o no
 */
public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length < 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }
        DatagramSocket datasocket=null;
        try {
            // Create a datagram socket
             datasocket= new DatagramSocket(Integer.parseInt(argv[0]));
            // Set maximum timeout to 300 secs
            datasocket.setSoTimeout(300000);
            while (true) {
                // Prepare datagram for reception
                byte array[] = new byte[1024];
                DatagramPacket dgramRec = new DatagramPacket(array, array.length);
                datasocket.receive(dgramRec);

                System.out.println("SERVER: Received "
                        + new String(dgramRec.getData(), 0, dgramRec.getLength())
                        + " from " + dgramRec.getAddress().toString() + ":"
                        + dgramRec.getPort());
                // Prepare datagram to send response
                String message = new String(dgramRec.getData(), 0, dgramRec.getLength());
                DatagramPacket dgramSent = new DatagramPacket(message.getBytes(),
                        message.getBytes().length,dgramRec.getAddress(),dgramRec.getPort());
                // Send response
                datasocket.send(dgramSent);
                System.out.println("SERVER: Sending "
                        + message + " to "
                        + dgramSent.getAddress().toString() + ":"
                        + dgramSent.getPort());
            }
          
         //Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
        // Close the socket
            datasocket.close();
        }
    }
}
