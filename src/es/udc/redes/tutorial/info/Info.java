package es.udc.redes.tutorial.info;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;


public class Info {
    public static void main(String[] args) throws IOException {
        BasicFileAttributes atributos;
        if (args[0].length() < 2) {
            System.out.println("Ruta no valida\n");
        } else {
            Path fich = Paths.get(args[0]);
            File aux = new File(args[0]);
            atributos= Files.readAttributes(fich, BasicFileAttributes.class);
            String extension="";
            String last_access=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(aux.lastModified());
            int last_punto;


            last_punto = args[0].lastIndexOf('.');
            extension = args[0].substring(last_punto+1);

            System.out.println("Size:" + atributos.size() +
                    "\nLast Access:" + last_access +
                    "\nName:" + fich.getFileName() +
                    "\nExtension:" + extension +
                    "\nType:" + Files.probeContentType(fich) + //MIME TYPE
                    "\nAbsolute Path:" + aux.getAbsolutePath());
        }
    }
}
