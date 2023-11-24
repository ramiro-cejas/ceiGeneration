package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteAttribute;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeVariableStaticMethod extends NodeVariable{

    private Token className;

    boolean alreadyChecked = false;
    public ConcreteMethod methodToCall;

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
                    methodToCall = methodToMatch;
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
        System.out.println("Generating static method TODO");
        //si es distinto de void, reservar memoria para el return
        if(!methodToCall.type.getName().equals("keyword_void")) {
            String cRet = " # We reserve a memory cell for the method's return value";
            codeGenerator.gen("RMEM 1" + cRet);
        }

        for(Node argument : parameters) {
            argument.generate(codeGenerator);
        }

        String tag = methodToCall.getTag();
        String cTag = " # We push the static method's tag to the top of the stack";
        codeGenerator.gen("PUSH " + tag + cTag);

        String cCall = " # We make the call.";
        codeGenerator.gen("CALL" + cCall);
    }
}
