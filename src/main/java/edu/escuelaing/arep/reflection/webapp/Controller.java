package edu.escuelaing.arep.reflection.webapp;

import edu.escuelaing.arep.reflection.springplus.Component;
import edu.escuelaing.arep.reflection.springplus.Service;

//@Component
public class Controller {

    @Service(uri="/cuadrado")
    public static double cuadrado(){
        return 2.0*2.0;
    }

    @Service(uri="/leon")
    public static String leon(){
        return "leon.jpg";
    }

    @Service(uri="/jaguar")
    public static String jaguar(){
        return "jaguar.jpg";
    }

    @Service(uri="/leopardo")
    public static String leopardo(){
        return "leopardo.jpeg";
    }

    @Service(uri="/tigre")
    public static String tigre(){
        return "tigre.jpg";
    }
}
