package SecondSemantic;

import SecondSemantic.FileManipulator.FileManipulator;
import SecondSemantic.Lexical.LexicalAnalyzer;
import SecondSemantic.Syntax.SyntaxAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class MainSem {
    public static void main(String[] args2) {
        String [] args = {"resources/prueba.java"};
        long actual = System.currentTimeMillis();
        boolean verbose = false;
        //borrar si existe el archivo ubicado en generation/a.out
        File archivo = new File("generation/a.out");
        // Intentar borrar el archivo
        if (archivo.delete()) {
            System.out.println("Archivo borrado con éxito.");
        } else {
            System.out.println("No se pudo borrar el archivo.");
        }
        if (args.length == 0){
            System.out.println("ERROR: Ningun archivo fuente pasado como parámetro. Por favor proporcione la ruta del archivo como parámetro.");
        }
        else{
            if (args.length > 1 && args[1].equals("-v")){
                verbose = true;
            }
            ArrayList<Exception> errorsCollection = new ArrayList<>();
            System.out.println("Se va a ejecutar el analizador semantico en el archivo: "+args[0]);
            try {
                FileManipulator fileManipulator = new FileManipulator(args[0]);
                LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(fileManipulator);
                SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);
                if (verbose){
                    syntaxAnalyzer.enableVerbose();
                }
                try {
                    syntaxAnalyzer.analyze();
                    if (verbose){
                        System.out.println("\n---------------------------------------------------------\n\n[Tabla de simbolos]");
                        System.out.println(syntaxAnalyzer.getST());
                    }
                    syntaxAnalyzer.generate();
                    errorsCollection.addAll(syntaxAnalyzer.getErrors());
                }catch (Exception e){
                    errorsCollection.add(e);
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.out.println("Error al abrir o leer el archivo.");
            }

            if (errorsCollection.isEmpty()){
                System.out.println("\n---------------------------------------------------------\n\n[SinErrores]");
            }else {
                for (Exception lexicalError : errorsCollection) {
                    System.out.println(lexicalError.toString());
                }
                System.out.println("\nCantidad de errores totales encontrados: "+errorsCollection.size());
            }
        }
        System.out.println("La ejecución a tardado: "+(System.currentTimeMillis() - actual)+"ms");
    }
}