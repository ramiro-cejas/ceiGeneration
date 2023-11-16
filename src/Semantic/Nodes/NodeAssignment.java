package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeAssignment implements Node{
    public Token sign;
    public Node leftExpression;
    public Node rightExpression;
    public NodeBlock parentBlock;
    public Token type;
    public boolean alreadyChecked = false;

    public NodeAssignment(Token sign, Node leftExpression, Node rightExpression, NodeBlock parentBlock) {
        this.sign = sign;
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        System.out.println("Checking assignment");
        if (!alreadyChecked){
            leftExpression.check(symbolTable);
            rightExpression.check(symbolTable);
            if (!(leftExpression instanceof NodeVariable)){
                symbolTable.semExceptionHandler.show(new SemanticException(sign, "Cannot assign to a non variable"));
            }
            if (rightExpression.getType().getLexeme().equals("void"))
                symbolTable.semExceptionHandler.show(new SemanticException(sign, "Cannot assign void to a variable"));
            //if le right side conform to the left side then it's ok
            if (!leftExpression.getType().getName().equals(rightExpression.getType().getName())){
                symbolTable.semExceptionHandler.show(new SemanticException(sign, "Cannot assign " + rightExpression.getType().getLexeme() + " to " + leftExpression.getType().getLexeme()));
            } else {
                //check if the left side type is a subtype of the type on the left side
                if (leftExpression.getType().getName().equals("idClass")){
                    // get the concrete class of the left side
                    ConcreteClass leftClass = symbolTable.classes.get(leftExpression.getType().getLexeme());
                    if (leftClass == null){
                        leftClass = symbolTable.interfaces.get(leftExpression.getType().getLexeme());
                    }
                    // get the concrete class of the right side
                    ConcreteClass rightClass = symbolTable.classes.get(rightExpression.getType().getLexeme());
                    if (rightClass == null){
                        rightClass = symbolTable.interfaces.get(rightExpression.getType().getLexeme());
                    }
                    if (!rightClass.isSubTypeOf(leftClass)){
                        symbolTable.semExceptionHandler.show(new SemanticException(sign, "Cannot assign " + rightExpression.getType().getLexeme() + " to " + leftExpression.getType().getLexeme()));
                    }
                }
            }

            type = leftExpression.getType();
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
        return sign;
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        //TODO
    }
}
