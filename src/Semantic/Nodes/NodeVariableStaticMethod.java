package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeVariableStaticMethod extends NodeVariable{

    private Token className;

    boolean alreadyChecked = false;

    public NodeVariableStaticMethod(Token name, NodeBlock parentBlock, Token className) {
        super(name, parentBlock);
        this.className = className;
    }

    public void check(SymbolTable symbolTable) throws SemanticException {
        if (alreadyChecked)
            return;
        //check if the class exist in the symboltable and check if that class has the static method
        if (symbolTable.classes.containsKey(className.getLexeme())){
            if (symbolTable.classes.get(className.getLexeme()).methods.containsKey(name.getLexeme())){
                //check if the parameters are the same
                ConcreteMethod methodToMatch = symbolTable.classes.get(className.getLexeme()).methods.get(name.getLexeme());
                if (methodToMatch.parameters.size() != parameters.size() || !methodToMatch.isStatic.getName().equals("keyword_static")){
                    symbolTable.semExceptionHandler.show(new SemanticException(name,"Static method " + name.getLexeme() + " is not defined in class " + className.getLexeme() + " with the given parameters"));
                } else {
                    for (int i = 0; i < parameters.size(); i++){
                        parameters.get(i).check(symbolTable);
                        if (!parameters.get(i).getType().getLexeme().equals(methodToMatch.parametersInOrder.get(i).getType().getLexeme())){
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Static method " + name.getLexeme() + " is not defined in class " + className.getLexeme() + " with the given parameters"));
                        }
                    }
                    type = methodToMatch.type;
                }
            } else {
                symbolTable.semExceptionHandler.show(new SemanticException(name,"Static method " + name.getLexeme() + " is not defined in class " + className.getLexeme()));
            }
        } else {
            symbolTable.semExceptionHandler.show(new SemanticException(name,"Static method " + name.getLexeme() + " is not defined"));
        }

        if (childChain != null){
            childChain.check(symbolTable);
            type = childChain.getType();
        }
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        //TODO
    }
}
