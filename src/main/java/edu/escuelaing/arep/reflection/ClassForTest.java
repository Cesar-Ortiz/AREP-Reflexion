package edu.escuelaing.arep.reflection;

public class ClassForTest {
    @Test
    public static void A(){
        C();
    }

    @Test
    public static void B() throws Exception{

    }

    public static void C(){}
    @Test
    public static void D() throws Exception{
        throw(new Exception("Error in D."));
    }
}
