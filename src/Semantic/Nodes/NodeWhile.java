package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Generation.TagHandler;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeWhile implements Node{

    public Token whileTok;
    public Node condition;
    public Node body;
    public NodeBlock parentBlock;
    private boolean alreadyChecked = false;

    public NodeWhile(Token whileTok, Node condition, Node body, NodeBlock parentBlock) {
        this.whileTok = whileTok;
        this.condition = condition;
        this.body = body;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (!alreadyChecked){
            condition.check(symbolTable);
            if (!condition.getType().getName().equals("keyword_boolean")){
                symbolTable.semExceptionHandler.show(new SemanticException(whileTok,"Condition must be boolean in line "+condition.getType().getRow()));
            }
            body.check(symbolTable);
            alreadyChecked = true;
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
        return whileTok;
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        System.out.println("Generating while");
        int id = TagHandler.getSentenceID();
        String tagIn = "while_" + id;
        String tagOut = "out_while_" + id;

        codeGenerator.gen(tagIn + ": NOP");
        condition.generate(codeGenerator);
        codeGenerator.gen("BF " + tagOut);
        body.generate(codeGenerator);
        codeGenerator.gen("JUMP " + tagIn);
        codeGenerator.gen(tagOut + ": NOP");

    }

    @Override
    public void assignOffsets() {
        //TODO
    }

    @Override
    public int getOffset() {
        //TODO check if this is correct
        return 0;
    }
}
