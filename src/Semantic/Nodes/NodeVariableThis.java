package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeVariableThis extends NodeVariable{

    public ConcreteClass thisClass;
    public NodeBlock parentBlock;
    private Token type;

    public NodeVariableThis(Token thisTok, ConcreteClass thisClass, NodeBlock parentBlock) {
        super(thisTok, parentBlock);
        this.thisClass = thisClass;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        type = thisClass.name;
        if (childChain != null){
            childChain.check(symbolTable);
            type = childChain.getType();
        }
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
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        System.out.println("Generating this TODO");
        //TODO
    }
}
