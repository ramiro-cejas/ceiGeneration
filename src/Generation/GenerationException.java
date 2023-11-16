package SecondSemantic.Generation;

import SecondSemantic.Extras.CompiException;

public class GenerationException extends CompiException {
    private String message;
    public GenerationException(String message) {
        this.message = message;
    }

    public String toString(){
        return "---------------------------------------------------------\n"+
                "Se ha producido una excepción de generación de código. "+message+"\n"+
                "---------------------------------------------------------";
    }
}
