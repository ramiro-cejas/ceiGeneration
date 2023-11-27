package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeUnaryOp extends NodeExpression {

    public Token unaryOp;
    public Node expression;
    private NodeBlock parentBlock;
    private boolean alreadyChecked = false;
    public Token type;

    public NodeUnaryOp(Token unaryOp, Node expression, NodeBlock parentBlock) {
        this.unaryOp = unaryOp;
        this.expression = expression;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (!alreadyChecked){
            expression.check(symbolTable);
            if (unaryOp.getLexeme().equals("!")) {
                if (!expression.getType().getName().equals("keyword_boolean")) {
                    symbolTable.semExceptionHandler.show(new SemanticException(unaryOp, "Unary operator ! must be applied to boolean expressions"));
                }
                type = expression.getType();
            } else if (unaryOp.getLexeme().equals("-") || unaryOp.getLexeme().equals("+")) {
                if (!expression.getType().getLexeme().equals("int") && !expression.getType().getLexeme().equals("float")) {
                    symbolTable.semExceptionHandler.show(new SemanticException(unaryOp, "Unary operator - or + must be applied to int or float expressions"));
                }
            } else {
                // This should never happen, but just in case
                symbolTable.semExceptionHandler.show(new SemanticException(unaryOp, "Unary operator " + unaryOp.getLexeme() + " is not valid"));
            }
        }
        alreadyChecked = true;
    }

    @Override
    public Token getType() {
        return expression.getType();
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }

    @Override
    public Token getToken() {
        return unaryOp;
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        System.out.println("Generating unary op");
        expression.generate(codeGenerator);

        switch (unaryOp.getLexeme()) {
            case "!":
                codeGenerator.gen("NOT");
                break;
            case "-":
                codeGenerator.gen("NEG");
                break;
        }
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
}
