package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeVariableConstructor extends NodeVariable{

    private boolean alreadyChecked = false;

    public NodeVariableConstructor(Token name, NodeBlock parentBlock, Token type) {
        super(name, parentBlock);
        this.type = type;
    }

    public void check(SymbolTable symbolTable) throws SemanticException {
        if (alreadyChecked)
            return;
        for (Node parameter : parameters){
            parameter.check(symbolTable);
        }
        if (symbolTable.classes.containsKey(type.getLexeme())){
            if (symbolTable.classes.get(type.getLexeme()).constructor == null){
                symbolTable.semExceptionHandler.show(new SemanticException(type,"Class " + type.getLexeme() + " has no constructor"));
            } else {
                if (symbolTable.classes.get(type.getLexeme()).constructor.parameters.size() != parameters.size()){
                    symbolTable.semExceptionHandler.show(new SemanticException(type,"Constructor of class " + type.getLexeme() + " has different number of parameters"));
                } else {
                    for (int i = 0; i < parameters.size(); i++){
                        //check if the parameter is a subtype of the parameter in the constructor
                        //if the parameter is a class, check if it's a subtype of the parameter in the constructor
                        if (parameters.get(i).getType().getName().equals("idClass")){
                            if (!symbolTable.classes.get(parameters.get(i).getType().getLexeme()).isSubTypeOf(symbolTable.classes.get(symbolTable.classes.get(type.getLexeme()).constructor.parametersInOrder.get(i).getType().getLexeme()))){
                                symbolTable.semExceptionHandler.show(new SemanticException(type,"Parameter " + i + " is not a subtype of the parameter in the constructor"));
                            }
                        } else {
                            if (!parameters.get(i).getType().getLexeme().equals(symbolTable.classes.get(type.getLexeme()).constructor.parametersInOrder.get(i).getType().getLexeme())){
                                symbolTable.semExceptionHandler.show(new SemanticException(type,"Parameter " + i + " is not the same type as the parameter in the constructor"));
                            }
                        }
                    }
                }
            }
            if(childChain != null){
                childChain.check(symbolTable);
                type = childChain.type;
            }
        } else {
            symbolTable.semExceptionHandler.show(new SemanticException(type,"Type " + type.getLexeme() + " is not a defined class"));
        }
        alreadyChecked = true;
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        //TODO
    }
}
