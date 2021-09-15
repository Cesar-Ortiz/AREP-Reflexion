package edu.escuelaing.arep.reflection.webapp;

import edu.escuelaing.arep.reflection.httpserver.HttpServer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppStarter {

    public static void main(String[] args){
        try {
            HttpServer._instance.start(args);
        } catch (IOException e) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, e);
        } catch (URISyntaxException e) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
