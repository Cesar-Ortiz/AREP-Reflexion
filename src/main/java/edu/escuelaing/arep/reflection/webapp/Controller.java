package edu.escuelaing.arep.reflection.webapp;

import edu.escuelaing.arep.reflection.springplus.Component;
import edu.escuelaing.arep.reflection.springplus.Service;

//@Component
public class Controller {

    @Service(uri="/cuadrado")
    public static double cuadrado(){
        return 2.0*2.0;
    }

    @Service(uri="/leo")
    public static String leo(){
        return "leon.jpg";
    }
}
