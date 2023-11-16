package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SymbolTable;

public abstract class NodeExpression implements Node{

    public Node expression;
    private NodeBlock parentBlock;

}
