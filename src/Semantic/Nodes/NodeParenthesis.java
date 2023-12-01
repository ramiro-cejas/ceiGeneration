package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeParenthesis extends NodeVariable{

    public Token sign;
    public Node expression;

    public NodeParenthesis(Token sign, Node expression, NodeBlock parentBlock) {
        this.sign = sign;
        this.expression = expression;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        expression.check(symbolTable);
        this.type = expression.getType();

        if (expression instanceof NodeVariableConstructor && !((NodeVariableConstructor) expression).inTheLastIsMethod()){

        }

        if (childChain != null){
            childChain.check(symbolTable);
            this.type = childChain.getType();
        }

    }

    @Override
    public Token getToken() {
        return sign;
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        System.out.println("Generating parenthesis");
        expression.generate(codeGenerator);
        if (childChain != null){
            childChain.generate(codeGenerator);
        }
    }
}
