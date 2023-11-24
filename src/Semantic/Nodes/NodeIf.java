package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeIf implements Node{
    public Token ifToken;

    public Node condition;
    public Node ifSentence;
    public Node elseSentence;
    public NodeBlock parentBlock;

    public NodeIf(Token ifToken, Node condition, Node ifSentence, Node elseSentence, NodeBlock parentBlock) {
        this.ifToken = ifToken;
        this.condition = condition;
        this.ifSentence = ifSentence;
        this.elseSentence = elseSentence;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        condition.check(symbolTable);
        if (!condition.getType().getLexeme().equals("boolean")){
            symbolTable.semExceptionHandler.show(new SemanticException(ifToken,"Condition must be boolean in line "+ condition.getType().getRow()));
        }
        ifSentence.check(symbolTable);
        if (elseSentence != null){
            elseSentence.check(symbolTable);
        }
    }

    @Override
    public Token getType() {
        return new Token("keyword_null", "null", -1);
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }

    @Override
    public Token getToken() {
        return ifToken;
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        System.out.println("Generating if TODO");
        //TODO
    }
}
