package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.*;

public class NodeVarDeclaration implements Node{

    public Token type;
    public Token id;
    public Node expression;
    private boolean alreadyChecked = false;
    private NodeBlock parentBlock;
    private ConcreteAttribute variable;

    public NodeVarDeclaration(Token id, Node expression, NodeBlock parentBlock) {
        this.variable = new ConcreteAttribute(id, type, new Token("", "", -1));
        variable.variableType = ConcreteAttribute.LOCAL_VARIABLE;
        this.id = id;
        this.expression = expression;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (alreadyChecked){
            return;
        }
        if (expression != null){
            expression.check(symbolTable);
            type = expression.getType();
            if (type.getLexeme().equals("null"))
                symbolTable.semExceptionHandler.show(new SemanticException(id, "Cannot assign null to a variable"));

            if (type.getLexeme().equals("void"))
                symbolTable.semExceptionHandler.show(new SemanticException(id, "Cannot assign void to a variable"));

            //check if already exists a local variable or a parameter with the same name

            for (ConcreteAttribute localVariable : parentBlock.localVariables) {
                if (localVariable.getName().getLexeme().equals(id.getLexeme())){
                    throw new SemanticException(id,"Variable " + id.getLexeme() + " already declared");
                }
            }
            for (ConcreteAttribute parameter : parentBlock.methodParameters) {
                if (parameter.getName().getLexeme().equals(id.getLexeme())){
                    throw new SemanticException(id,"Parameter " + id.getLexeme() + " already declared");
                }
            }
            ConcreteAttribute toAdd = new ConcreteAttribute(id, type, new Token("", "", -1));
            toAdd.variableType = ConcreteAttribute.LOCAL_VARIABLE;
            parentBlock.localVariables.add(toAdd);
            variable = toAdd;
        }
        alreadyChecked = true;
    }

    @Override
    public Token getType() {
        if (type == null){
            return new Token("void", "void", -1);
        }
        return type;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }

    @Override
    public Token getToken() {
        return id;
    }

    public String toString(){
        return "NodeVarDeclaration: "+type.getLexeme()+" "+id.getLexeme();
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {

        expression.generate(codeGenerator);

        int offset = variable.getOffset();


        String c = " # We store what's on top of the pile in the stack through localvar's offset";
        codeGenerator.gen("STORE " + offset + c);
    }

    @Override
    public void assignOffsets() {
        //TODO
    }

    @Override
    public int getOffset() {
        //TODO check if this is correct
        return variable.getOffset();
    }

    @Override
    public void setIsInLeftSideOfAnAssignment() {
        //do nothing
    }
}
