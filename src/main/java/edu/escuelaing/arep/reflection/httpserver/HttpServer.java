package edu.escuelaing.arep.reflection.httpserver;

import edu.escuelaing.arep.reflection.springplus.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {
    public static final HttpServer _instance = new HttpServer();
    private HashMap<String, String> extencionList = new HashMap<String, String>();
    private HashMap<String, Method> services = new HashMap<String, Method>();

    private static HttpServer getInstance(){
        return _instance;
    }
    private HttpServer(){}

    public void start(String[] args, int port) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        loadServices(args[0]);
        boolean running = true;
        while(running){
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            serveConnection(clientSocket);
        }
        serverSocket.close();
    }

    private void loadServices(String args){
        try{
            //String classPath="edu.escuelaing.arep.reflection.webapp.Controller";
            String classPath=args;
            Class c = Class.forName(classPath);

            for(Method m : c.getDeclaredMethods()){
                if(m.isAnnotationPresent(Service.class)){
                    String uri = m.getAnnotation(Service.class).uri();
                    services.put(uri, m);
                }
            }
        }catch (ClassNotFoundException ex){
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fullExtens(){
        extencionList.put("js", "text");
        extencionList.put("html", "text");
        extencionList.put("css", "text");
        extencionList.put("gif", "image");
        extencionList.put("jpeg", "image");
        extencionList.put("jpg", "image");
        extencionList.put("png", "image");
    }

    public void serveConnection(Socket clientSocket) throws IOException, URISyntaxException {
        fullExtens();
        OutputStream outputStream;
        outputStream = clientSocket.getOutputStream();
        PrintWriter out = new PrintWriter(outputStream, true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine, outputLine;
        ArrayList<String> request = new ArrayList<String>();

        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            request.add(inputLine);
            if (!in.ready()) {
                break;
            }
        }

        try{

            String uriStr= request.get(0).split(" ")[1];
            System.out.println("uriStr: "+uriStr);

            URI resourceURI = new URI(uriStr);

            if(resourceURI.toString().startsWith("/appuser")){
                outputLine = getComponentResource(resourceURI);
                out.println(outputLine);
            }

            else if(extencionList.get(typeExt(resourceURI.getPath())).equals("image")){
                getImageResource(resourceURI.getPath(), outputStream);
            }
            else if(extencionList.get(typeExt(resourceURI.getPath())).equals("text")){
                outputLine = getResource(resourceURI);
                out.println(outputLine);
            }
            else{
                outputLine = getResource(resourceURI);
                out.println(outputLine);
            }

        }
        catch (Exception e){
            out.println(computeDefaultResponse());
        }
        out.close();
        in.close();
        clientSocket.close();
    }

    private String getComponentResource(URI resourceURI) {
        String response= computeDefaultResponse();
        try{
            String classPath= resourceURI.getPath().toString().replaceAll("/appuser", "");
            if(services.containsKey(classPath)){
                response = services.get(classPath).invoke(null).toString();
                response="HTTP/1.1 200 OK\r\nContent - Type: text/html\r\n\r\n"+response;
            }
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getResource(URI resourceURI) throws IOException {
        System.out.println("Received URI path: "+resourceURI.getPath());
        System.out.println("Received URI query: "+resourceURI.getQuery());
        return getTextResource(resourceURI.getPath());
    }

    private void getImageResource(String exten, OutputStream outputStream) throws IOException {
        String path = "src/main/resources/public"+ exten;
        File file = new File(path);

        if (file.exists()) {
            try {
                BufferedImage image = ImageIO.read(file);
                if (image != null) {
                    DataOutputStream imgWrite = new DataOutputStream(outputStream);
                    ByteArrayOutputStream streamoutput = new ByteArrayOutputStream();
                    String ext=typeExt(exten);
                    ImageIO.write(image, ext, streamoutput);
                    imgWrite.writeBytes("HTTP/1.1 200 OK \r\n" + "Content-Type: " + "image/"+ ext + "\r\n" + "\r\n");
                    imgWrite.write(streamoutput.toByteArray());
                }
            } catch (IOException e) {
                System.err.format("IOException: %s%n", e);
                throw new IOException("DataOutputStream it cant be empty!");
            }
        } else {
            throw new IOException("File no exist!");
        }
    }

    public String getTextResource(String extent) throws IOException{
        System.out.println("extención: "+extent);
        File archivo = new File("src/main/resources/public"+extent);
        BufferedReader in = new BufferedReader(new FileReader(archivo));
        String ext = typeExt(extent);
        String str;
        String output = "HTTP/1.1 200 OK\r\nContent - Type: text/"+ ext +"\r\n\r\n";
        while((str = in.readLine())!= null){
            System.out.println(str);
            output+=str+"\n";
        }
        System.out.println(output);

        return output;
    }

    public String typeExt(String path){
        String[] ext = path.split("\\.");
        return ext[1];
    }

    public String computeDefaultResponse(){
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Title of the document</title>\n"
                + "</head>"
                + "<body>"
                + "<h1>My Web Site</h1>"
                + "<br>"
                + "<img src=\"https://files.rcnradio.com/public/styles/image_834x569/public/2018-06/federacioncolombianadefutbol_0.jpg?itok=KhQ50TPY\"></img>"
                + "</body>"
                + "</html>";
        return outputLine;
    }

}
