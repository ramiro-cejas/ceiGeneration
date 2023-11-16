package SecondSemantic.Lexical;

import SecondSemantic.Extras.CompiException;

public class LexicalException extends CompiException {
    String lexeme, error, lineContent;
    int line,column;

    public LexicalException(String lexeme, int line, int column) {
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
        error = "";
    }

    public LexicalException(String lexeme, int line, int column, String error, String lineContent) {
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
        this.error = error;
        this.lineContent = lineContent;
    }

    public String toString(){
        String toReturn = "---------------------------------------------------------\n";
        toReturn += "Se ha producido una excepción léxica. "+error+" \nLexema: "+lexeme+" En la línea: "+line+", columna: "+column;
        toReturn += "\n\n";
        toReturn += lineContent;
        toReturn += "\n";
        for (int i=0; i<column-1; i++){
            toReturn += " ";
        }
        toReturn += "↑\n\n";
        toReturn += generateErrorCode();
        toReturn += "\n---------------------------------------------------------";

        return toReturn;
    }

    private String generateErrorCode(){
        return "[Error:"+lexeme+"|"+line+"]";
    }
}
