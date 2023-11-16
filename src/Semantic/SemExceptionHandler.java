package SecondSemantic.Semantic;

public class SemExceptionHandler {
    SymbolTable symbolTable;
    public SemExceptionHandler(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    public void show(SemanticException e) throws SemanticException {
        //symbolTable.errors.add(e);
        //this because the multi detection of errors will be implemented in the future
        throw e;
    }
}
