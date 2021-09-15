package edu.escuelaing.arep.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;

public class InvokeMain {

    public static void main(String[] args) {

        try {
            String rutaClase = "edu.escuelaing.arep.reflection.ReflectionExamples";
            //Class<?> c = Class.forName(args[0]);
            Class<?> c = Class.forName(rutaClase);
            Class[] argTypes = new Class[] { Member[].class , String.class};
            Method printMembersMethod = c.getDeclaredMethod("printMembers", argTypes);
            //String[] mainArgs = Arrays.copyOfRange(args, 1, args.length);
            Member[] classListOfMembers = Integer.class.getDeclaredMethods();
            String s = "Methods";
            Object[] invokeArgs = new Object[]{classListOfMembers, s};
            System.out.format("invoking %s.printMembers()%n", c.getName());
            printMembersMethod.invoke(null, invokeArgs);

            // production code should handle these exceptions more gracefully

        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        } catch (NoSuchMethodException x) {
            x.printStackTrace();
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        } catch (InvocationTargetException x) {
            x.printStackTrace();
        }

    }
}
