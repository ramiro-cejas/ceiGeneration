package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeReturn implements Node{
    public Token returnTok;
    public Node expression;
    private Token type;
    private boolean alreadyChecked = false;
    private NodeBlock parentBlock;

    public NodeReturn(Token returnTok, Node expression, NodeBlock parentBlock) {
        this.returnTok = returnTok;
        this.expression = expression;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (alreadyChecked){
            return;
        }
        //if im in the constructor method then I don't need to check for return
        if (parentBlock.currentMethod == parentBlock.currentClass.constructor){
            if (expression != null){
                expression.check(symbolTable);
                if (!expression.getType().getLexeme().equals("void")){
                    symbolTable.semExceptionHandler.show(new SemanticException(returnTok,"Constructor method cannot have a return statement"));
                }
            }
            alreadyChecked = true;
            return;
        }
        if (expression != null){
            expression.check(symbolTable);
            type = expression.getType();
        }
        else{
            type = new Token("keyword_void", "void", -1);
        }
        if (!type.getLexeme().equals("null")) {
            if (type.getName().equals("idClass")) {
                // type is subtype of the return type declared in the method
                ConcreteClass classOfReturn = symbolTable.classes.get(type.getLexeme());
                if (classOfReturn == null) {
                    classOfReturn = symbolTable.interfaces.get(type.getLexeme());
                }
                if (classOfReturn == null) {
                    symbolTable.semExceptionHandler.show(new SemanticException(returnTok, "Class or interface " + type.getLexeme() + " not found"));
                }

                //now get the type of return declared in the method
                ConcreteClass classOfMethod = symbolTable.classes.get(parentBlock.currentMethod.type.getLexeme());
                if (classOfMethod == null) {
                    classOfMethod = symbolTable.interfaces.get(parentBlock.currentMethod.type.getLexeme());
                }
                if (classOfMethod == null) {
                    symbolTable.semExceptionHandler.show(new SemanticException(returnTok, "Class or interface " + parentBlock.currentMethod.type.getLexeme() + " not found"));
                }

                //now we should have the concretes class, now check if the return type is a subtype of the method type
                if (!classOfReturn.isSubTypeOf(classOfMethod)) {
                    symbolTable.semExceptionHandler.show(new SemanticException(returnTok, "Class " + type.getLexeme() + " is not a subtype of " + parentBlock.currentMethod.type.getLexeme() + " in line " + type.getRow()));
                }

            } else if (!type.getLexeme().equals(parentBlock.currentMethod.type.getLexeme())) {
                symbolTable.semExceptionHandler.show(new SemanticException(returnTok, "Return type is not compatible with the return type of the method"));
            }
        } else{
            //if the return type of the method is a primitive type then error
            if (parentBlock.currentMethod.type.getLexeme().equals("int") || parentBlock.currentMethod.type.getLexeme().equals("float") || parentBlock.currentMethod.type.getLexeme().equals("char") || parentBlock.currentMethod.type.getLexeme().equals("boolean")) {
                symbolTable.semExceptionHandler.show(new SemanticException(returnTok, "Return type is not compatible with the return type of the method"));
            }
        }
        alreadyChecked = true;
    }

    @Override
    public Token getType() {
        return type;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }

    @Override
    public Token getToken() {
        return returnTok;
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        if (parentBlock.currentMethod.type.getName().equals("keyword_void") || parentBlock.currentMethod == parentBlock.currentClass.constructor){
            generateExit(codeGenerator);
        } else {
            generateReturn(codeGenerator);
            generateExit(codeGenerator);
        }

    }

    private void generateReturn(CodeGenerator codeGenerator) throws CompiException {
        int parameterCount = parentBlock.methodParameters.size();
        int dynamicBonus = parentBlock.currentMethod.isStatic.getName().equals("keyword_static") ? 0 : 1;

        int returnCellOffset = 3 + parameterCount + dynamicBonus;

        expression.generate(codeGenerator);

        String cReturn = " # We save the return value in the space reserved for it";
        codeGenerator.gen("STORE " + returnCellOffset + cReturn);
    }

    private void generateExit(CodeGenerator codeGenerator) throws CompiException {
        int parentBlockLastOffset = parentBlock.getOffset();
        int localVarCount = -parentBlockLastOffset;

        int parameterCount = parentBlock.currentMethod.parameters.size();
        int dynamicBonus = parentBlock.currentMethod.isStatic.getName().equals("keyword_static") ? 0 : 1;

        int retParameter = parameterCount + dynamicBonus;

        if(localVarCount > 0) {
            String cFree = " # On return: We free the space used by local vars";
            codeGenerator.gen("FMEM " + localVarCount + cFree);
        }

        String cStoreFP = " # On return: We point FP to caller's AR";
        codeGenerator.gen("STOREFP" + cStoreFP);

        String cRet = " # On return: We free up memory cells equal to number of params [+1 if unit is dynamic]";
        codeGenerator.gen("RET " + retParameter + cRet);
    }

    @Override
    public void assignOffsets() {
        //TODO
    }

    @Override
    public int getOffset() {
        //TODO
        return 0;
    }

    @Override
    public void setIsInLeftSideOfAnAssignment() {
        //do nothing
    }
}
