package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) throws Exception {
        // create socket at defined port
        int port = 8989;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening at port: " + port);

        // wait for connections and process
        while (true) {
            // create a client socket
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String request = in.readLine(); // Now you get GET index.html HTTP/1.1
            String[] requestParam = request.split(" ");

            String s;

            System.out.println("-- --- ---- ----- About to try and print clients message: ----- ---- --- --");
            while ((s = in.readLine()) != null) {
                System.out.println(s);
                if (s.isEmpty()) {
                    break;
                }
            }

            System.out.println("----- ---- --- -- Finished printing -- --- ---- -----");


            // After closing
            System.err.println("Client connection completed!");
            out.close();
            in.close();
            clientSocket.close();
        }
    }
}
