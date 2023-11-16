package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeBinaryOp extends NodeExpression{
    public Node leftExpression;
    public Node rightExpression;
    public Token operator;
    public NodeBlock parentBlock;
    private Token type;
    public boolean alreadyChecked = false;

    public NodeBinaryOp(Token operator, Node leftExpression, Node rightExpression, NodeBlock parentBlock) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.operator = operator;
        this.parentBlock = parentBlock;
    }
    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (alreadyChecked){
            return;
        }
        leftExpression.check(symbolTable);
        rightExpression.check(symbolTable);
        Token leftType = leftExpression.getType();
        Token rightType = rightExpression.getType();
        //if the operator is + - * / % only int and float are allowed (check with the lexeme of the token)
        if (operator.getLexeme().equals("+") || operator.getLexeme().equals("-") || operator.getLexeme().equals("*") || operator.getLexeme().equals("/") || operator.getLexeme().equals("%")){
            // if the types are different then error
            if (!leftType.getLexeme().equals(rightType.getLexeme())){
                symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation between different types"));
            }
            if (!leftType.getLexeme().equals("int") && !leftType.getLexeme().equals("float")){
                symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation only allowed between int and float"));
            }
            type = leftType;
        } else
            //if the operator is && || only boolean is allowed (check with the lexeme of the token)
            if (operator.getLexeme().equals("&&") || operator.getLexeme().equals("||")){
                // if the types are different then error
                if (!leftType.getLexeme().equals(rightType.getLexeme())){
                    symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation between different types"));
                }
                if (!leftType.getLexeme().equals("boolean")){
                    symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation only allowed between boolean"));
                }
                type = leftType;
            }
            else
                //if the operator is == != only conform types are allowed (check with the lexeme of the token)
                //if the type of the right conform with the left one, then the type will be boolean, otherwise error
                if (operator.getLexeme().equals("!=") || operator.getLexeme().equals("==")){
                    if (leftType.getLexeme().equals("int") || leftType.getLexeme().equals("float") || leftType.getLexeme().equals("char") || leftType.getLexeme().equals("boolean")){
                        if (!leftType.getLexeme().equals(rightType.getLexeme())){
                            symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation between different types"));
                        }
                        type = new Token("keyword_boolean", "boolean", operator.getRow());
                    } else {
                        if (symbolTable.classes.get(rightType.getLexeme()).isSubTypeOf(symbolTable.classes.get(leftType.getLexeme()))){
                            type = new Token("keyword_boolean", "boolean", operator.getRow());
                        } else {
                            symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation between different types"));
                        }
                    }
                } else {
                    //if the operator is < > <= >= only int and float are allowed (check with the lexeme of the token)
                    //if the types are different then error
                    if (!leftType.getLexeme().equals(rightType.getLexeme())){
                        symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation between different types"));
                    }
                    if (!leftType.getLexeme().equals("int") && !leftType.getLexeme().equals("float")){
                        symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation only allowed between int and float"));
                    }
                    type = new Token("keyword_boolean", "boolean", operator.getRow());
                }
        alreadyChecked = true;
    }

    @Override
    public Token getType() {
        return type;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {

    }

    @Override
    public Token getToken() {
        return operator;
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        //TODO
    }
}
