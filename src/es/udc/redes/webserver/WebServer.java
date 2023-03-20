package es.udc.redes.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServer {
    public static void main(String[] args) {

        ServerSocket server = null;
        try {
            server = new ServerSocket(Integer.parseInt(args[0]));
            server.setSoTimeout(300000);
            System.out.println("Server listening on port " + args[0]);
            System.out.println("Server Name: PAQUITO");
            while (true) {

                Socket client = server.accept();
                System.out.println("\nClient connected from: " + client.getInetAddress()
                        + ":" + client.getPort());
                ServerThread serverT = new ServerThread(client);
                serverT.start();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
