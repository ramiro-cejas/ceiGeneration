package SecondSemantic;

import SecondSemantic.FileManipulator.FileManipulator;
import SecondSemantic.Lexical.LexicalAnalyzer;
import SecondSemantic.Syntax.SyntaxAnalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.File;


class MainGeneration {
    public static void main(String[] args2) {
        //execute the MainSem
        MainSem.main(args2);
        //execute the VirtualMachine
        // Comando a ejecutar
        //String comando = "java -jar CeIVM2023/CeIVM-cei2011.jar generation/a.out";
        /*
        try {
            // Ejecutar el comando
            Process proceso = Runtime.getRuntime().exec(comando);

            // Crear un lector de entrada con la codificación adecuada para capturar la salida del proceso
            BufferedReader lector = new BufferedReader(new InputStreamReader(proceso.getInputStream(), StandardCharsets.UTF_8));

            // Leer y mostrar la salida del proceso línea por línea
            String linea;
            while ((linea = lector.readLine()) != null) {
                System.out.println(linea);
            }

            // Esperar a que el proceso termine
            int resultado = proceso.waitFor();

            // Imprimir el resultado
            if (resultado == 0) {
                System.out.println("Comando ejecutado correctamente.");
            } else {
                System.out.println("Error al ejecutar el comando. Código de salida: " + resultado);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}