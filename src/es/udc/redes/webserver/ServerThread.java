package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket s) {
        // Store the socket s
        this.socket = s;
    }

    @Override
    public void run() {
        try {
            socket.setSoTimeout(300000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();
            String request = in.readLine();
            String host= in.readLine();
            String if_mod;

            while ( (if_mod = in.readLine()) != null && !if_mod.equals("")){
                if (if_mod.contains("If-Modified-Since:")) {
                    break;
                }
            }

            String code_http;
            String[] tokens;
            File fich;
            // This code processes HTTP requests and generates HTTP responses
            System.out.println(request+"\n"+host+"\n"+if_mod+"\n");
            tokens = request.split(" ");
            if (tokens.length<3 || !tokens[2].startsWith("HTTP/1.0")) { //Malas Peticiones faltan argumentos
                fich = new File("p1-files/error400.html");
                code_http="400 Bad Request";
                if (tokens[0].equals("GET")) getInfoFile(fich, tokens[2], out, code_http,true);
                else getInfoFile(fich, tokens[2], out, code_http,false);
            } else {//Peticion Bien Formada
                fich = new File("p1-files" + tokens[1]);
                if (tokens[0].equals("GET")) {//Caso de GET
                    if (!fich.exists()) {//Fichero no existe en p1-files
                        code_http="404 Not Found";
                        fich = new File("p1-files/error404.html");
                        getInfoFile( fich, tokens[2], out, code_http, true);
                    } else {//Fichero existe en p1-files
                        code_http="200 OK";
                        if(if_mod.contains("If-Modified-Since")){//Caso de If-Modified-Since
                            Instant solicitud = Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(if_mod.substring(if_mod.indexOf(" ")+1)));
                            Instant last_mod = Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(format_Date(fich.lastModified())));
                            if(solicitud.compareTo(last_mod)>=0){ //Archivo No Modificado
                                code_http="304 Not Modified";
                                getInfoFile(fich,tokens[2],out,code_http,false);
                            }
                        }
                        //Casos con GET o Archivo Modificado
                        getInfoFile( fich, tokens[2], out, code_http, true);
                    }
                }

                else if (tokens[0].equals("HEAD")) {//Caso de HEAD
                    code_http="200 OK";
                    //Compruba existencia fichero y envia la informacion sobre este
                    if (fich.exists()) getInfoFile(fich, tokens[2], out, code_http, false);
                    else {//Si no existe el fichero
                        code_http="404 Not Found";
                        fich = new File("p1-files/error404.html");
                        //Mandamos la informacion sobre el error404.html
                        getInfoFile(fich, tokens[2], out, code_http, false);
                    }
                }
                else{//Casos q no son HEAD ni GET
                    code_http="400 Bad Request";
                    fich=new File("p1-files/error400.html");
                    getInfoFile(fich,tokens[2],out,code_http,true);
                }
            }

        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getInfoFile(File fich, String htttpVersion, OutputStream out, String code_http,boolean isGet) throws IOException {
        byte[] content = Files.readAllBytes(fich.toPath());
        String fechaFormateada = format_Date(fich.lastModified());

        out.write((htttpVersion+" "+code_http+"\n").getBytes());
        out.write(("Date: "+format_Date(0)+"\n").getBytes());
        out.write(("Server: PAQUITO \n").getBytes());
        out.write(("Last-Modified: "+fechaFormateada+"\n").getBytes());
        out.write(("Content-Length: "+fich.length()+"\n").getBytes());
        out.write(("Content-Type: "+Files.probeContentType(fich.toPath())+"\n\n").getBytes());
        if(isGet)out.write(content);
    }

    private String format_Date(long last_mod){
        Date fecha = new Date(last_mod);
        if(last_mod==0) fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.US);
        formatoFecha.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatoFecha.format(fecha);
    }
}