package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by connor on 08/11/2016.
 */
public class Main {

    private static final int PORT = 8088;

    public static void main(String[] args) throws IOException {
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server listening at port: " + PORT);
            System.out.println("__________ __________ __________ __________");

            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("trying to create new client socket");
                new Thread(new SimpleServerThread(clientSocket)).start();
            }
        } catch (Exception e) {
            System.out.println("Exception in main");
            e.printStackTrace();
        }
    }


}
