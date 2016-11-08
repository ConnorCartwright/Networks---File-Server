package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by connor on 08/11/2016.
 */
public class SimpleServerThread implements Runnable {

        Socket clientSocket;

        public SimpleServerThread(Socket clientSocket) {
            System.out.println("----- ----- ----- ------ ------ ------");
            System.out.println("Client connected!");
            this.clientSocket = clientSocket;
        }

        private String readInput(BufferedReader in) {
            try {
                return in.readLine();

            } catch(IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private String getRequestHash(BufferedReader in) {
            String line = readInput(in);
            String hash = "";

            while (line != null && !line.isEmpty()) {
                String part = line.substring(0, line.lastIndexOf(':'));

                if (part.equals("If-None-Match")) { // get our hash for caching
                    hash = line.substring(line.lastIndexOf(':') + 2);
                }
                line = readInput(in);
            }

            return hash;
        }

        private String getResponseHash(byte[] output) {
            try {
                MessageDigest mdEnc = MessageDigest.getInstance("MD5");
                mdEnc.update(output, 0, output.length);
                return new BigInteger(1, mdEnc.digest()).toString(16); // Hash value
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void run() {
            BufferedReader in;
            OutputStream out;
            String request = "";
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = clientSocket.getOutputStream();
                request = readInput(in); // `GET <filepath> HTTP/1.1`

                String requestHash = "";
                String responseHash = "";

                String[] requestParam = request.split(" ");
                String path = requestParam[1].substring(1);

                if (path.length() < 1) {
                    path = "index.html";
                }

                String status = "";
                String headers = "";

                Path p = Paths.get("resources/" + path);
                boolean fileExists = Files.exists(p);
                boolean cached = false;

                CheckContentType cct = new CheckContentType(path.substring(path.lastIndexOf('.') + 1));

                byte[] output;

                System.out.println("Does file exist: " + fileExists);

                if (fileExists) {
                    output = Files.readAllBytes(p);

                    requestHash = getRequestHash(in);
                    responseHash = getResponseHash(output);

                    if (!responseHash.equals(requestHash)) {
                        status = "HTTP/1.1 200 OK\r\n";
                    } else {
                        System.out.println("Hashes match!");
                        status = "HTTP/1.1 304 NOT MODIFIED\r\n";
                        cached = true;
                    }
                } else {
                    System.out.println("File DOES NOT exist!");
                    status = "HTTP/1.1 404 NOT FOUND\r\n";
                    p = Paths.get("resources/redirect.html");
                    cct = new CheckContentType("html");
                    output = Files.readAllBytes(p);
                }

                String date = "Date: " + java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT"))) + "\r\n";
                String cType = cct.getContentType();
                String contentLength = "Content-Length: " + output.length + "\r\n";
                String contentType = "Content-Type: " + cType + "\r\n";
                String cacheControl = "Cache-Control:public, max-age=400000\r\n"; // set max age to one week, overrides expire
                String eTag = "ETag: " + responseHash + "\r\n"; // using eTag to check if file is the same
                String keepAlive = "Connection: keep-alive\r\n";
                headers = status + date + contentLength + contentType + cacheControl + eTag + keepAlive + "\r\n"; // extra newline at end
                out.write(headers.getBytes());

                if (!cached) {
                    out.write(output);
                }

                out.flush();
                out.close();
                in.close();
                clientSocket.close();
                System.out.println("Client connection completed successfully!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
