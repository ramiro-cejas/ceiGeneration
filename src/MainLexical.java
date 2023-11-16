package SecondSemantic;

import SecondSemantic.FileManipulator.FileManipulator;
import SecondSemantic.Lexical.LexicalAnalyzer;
import SecondSemantic.Lexical.LexicalException;
import SecondSemantic.Lexical.Token;

import java.io.IOException;
import java.util.*;

class MainLexical {
    public static void main(String[] args) {
        long actual = System.currentTimeMillis();
        if (args.length == 0){
            System.out.println("ERROR: Ningun archivo fuente pasado como parámetro. Por favor proporcione la ruta del archivo como parámetro.");
        }
        else{
            ArrayList<LexicalException> errorsCollection = new ArrayList<>();
            System.out.println("Se va a ejecutar el analizador léxico en el archivo: "+args[0]);
            try {
                FileManipulator fileManipulator = new FileManipulator(args[0]);
                LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(fileManipulator);

                Token current = new Token(null,null,0);

                while (current.getName() != "EOF"){
                    try {
                        current = lexicalAnalyzer.getNextToken();
                        System.out.println(current);
                    }catch (LexicalException e){
                        errorsCollection.add(e);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al abrir o leer el archivo.");
            }

            if (errorsCollection.isEmpty()){
                System.out.println("\n---------------------------------------------------------\n\n[SinErrores]");
            }else {
                for (LexicalException lexicalError : errorsCollection) {
                    System.out.println(lexicalError.toString());
                }
                System.out.println("\nCantidad de errores totales encontrados: "+errorsCollection.size());
            }
        }
        System.out.println("La ejecución a durado: "+(System.currentTimeMillis() - actual)+"ms");
    }
}