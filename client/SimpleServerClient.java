package client;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Created by connor on 07/11/2016.
 */
public class SimpleServerClient {

    public static void main(String[] args) {
        if (args.length > 0) {
            SimpleServerClient ssc = new SimpleServerClient(args[0]);
        } else {
            System.out.println("No URL specified!");
        }
    }

    public SimpleServerClient(String url) {
        Desktop d = Desktop.getDesktop();
        try { // try and get the live site
            URL u = new URL(url);
            d.browse(u.toURI());
            System.out.println("GET " +  url + " HTTP/1.1 200 OK");
        } catch (Exception e) {
            try { // else try and find the file locally
                File f = new File("resources/" + Paths.get(url));
                try {
                    d.edit(f);
                    System.out.println("GET " +  url + " HTTP/1.1 200 OK");
                    System.out.println("Resource found locally!");
                } catch (Exception e1) {
                    System.out.println("404, File Not Found");
                }

            } catch (Exception e2) { // else the file is neither live or local
                System.out.println("404, File Not Found");
            }
        }
    }
}
