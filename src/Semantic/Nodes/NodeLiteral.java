package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;
import SecondSemantic.Generation.TagHandler;

public class NodeLiteral implements Node{

    public Token literal;
    public NodeBlock parentBlock;
    private boolean alreadyChecked = false;
    private Token type;
    private SymbolTable symbolTable;
    private static int id = 0;

    public NodeLiteral(Token literal, NodeBlock parentBlock) {
        this.literal = literal;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        this.symbolTable = symbolTable;
        if (alreadyChecked)
            return;
        if (literal.getName().equals("keyword_null"))
            type = new Token("keyword_null", "null", literal.getRow());
        else if (literal.getName().equals("keyword_true") || literal.getName().equals("keyword_false"))
            type = new Token("keyword_boolean", "boolean", literal.getRow());
        else if (literal.getName().equals("intLiteral"))
            type = new Token("keyword_int", "int", literal.getRow());
        else if (literal.getName().equals("floatLiteral"))
            type = new Token("keyword_float", "float", literal.getRow());
        else if (literal.getName().equals("strLiteral"))
            type = new Token("idClass", "String", literal.getRow());
        else
            type = new Token("keyword_char", "char", literal.getRow());
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
        return literal;
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        System.out.println("Generating literal");
        if (type.getLexeme().equals("int"))
            genInt(codeGenerator);
        else if (type.getLexeme().equals("char"))
            genChar(codeGenerator);
        else if (type.getLexeme().equals("boolean"))
            genBoolean(codeGenerator);
        else if (type.getLexeme().equals("String"))
            genString(codeGenerator);
        else if (type.getLexeme().equals("null"))
            genNull(codeGenerator);
        else if (type.getLexeme().equals("float")){
            genFloat(codeGenerator);
        }
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

    private void genFloat(CodeGenerator codeGenerator) {
        //TODO: Implement this
    }

    private void genInt(CodeGenerator codeGenerator) throws CompiException {
        codeGenerator.gen("PUSH " + literal.getLexeme());
    }

    private void genChar(CodeGenerator codeGenerator) throws CompiException {
        String lexeme = literal.getLexeme();
        String lexemeWithoutQuotes = lexeme.substring(1, lexeme.length()-1);
        char charLiteral = lexemeWithoutQuotes.charAt(0);

        if(charLiteral == '\\') {
            charLiteral = lexemeWithoutQuotes.charAt(1);
        }

        codeGenerator.gen("PUSH " + (int) charLiteral);
    }

    private void genBoolean(CodeGenerator codeGenerator) throws CompiException {
        codeGenerator.gen("PUSH " + (literal.getName().equals("keyword_true") ? 1 : 0));
    }

    private void genString(CodeGenerator codeGenerator) throws CompiException {
        id++;

        ConcreteClass classConstructed = symbolTable.classes.get("String");

        String lexemeWithoutBackwardsBars = literal.getLexeme().replace("\\", "");
        String tag;

        if(lexemeWithoutBackwardsBars.equals("\"\"")) {
            tag = "str_empty";
        } else {
            tag = "str_" + id;
            codeGenerator.gen(".DATA");
            codeGenerator.gen(tag + ": DW " + lexemeWithoutBackwardsBars + ",0");
        }

        //We're doing an implicit constructor access here!
        codeGenerator.gen(".CODE");

        String cReserve = " # In String Literal: We reserve a memory cell to store the pointer to the constructed object";
        codeGenerator.gen("RMEM 1" + cReserve);

        String c2 = " # In String Literal: We load malloc's parameter (hardcoded)";
        codeGenerator.gen("PUSH 2"+ c2);

        String mallocTag = TagHandler.getMallocTag();

        String c3 = " # We put malloc's tag on top of the stack";
        codeGenerator.gen("PUSH " + mallocTag + c3);

        String c4 = " # We make the call";
        codeGenerator.gen("CALL" + c4);

        //Now, we must link the VTable of the class of the object that's being constructed to its CIR

        String c5 = " # We duplicate malloc's return so we don't lose it on our next instr.";
        codeGenerator.gen("DUP" + c5);

        String vTableTag = TagHandler.getVTableTag(classConstructed);

        String c6 = " # We push the VTable's tag so we can link it to the CIR.";
        codeGenerator.gen("PUSH " + vTableTag + c6);

        String c7 = " # We store the reference (tag) to the VTable in the CIR of the constructed object (the offset is hard-coded)";
        codeGenerator.gen("STOREREF 0" + c7);

        String c8 = " # We duplicate malloc's return and we use it as the 'this' reference for the constructor";
        codeGenerator.gen("DUP" + c8);

        //This is sort of a hard-coded constructor call. We don't *actually* call it,
        //but we manipulate the CIR as though we were the constructor.

        String c9 = " # We put the String tag at the top of the stack";
        codeGenerator.gen("PUSH " + tag + c9);

        String c10 = " # We link the tag to the 'fake attribute'";
        codeGenerator.gen("STOREREF 1" + c10);
    }

    private void genNull(CodeGenerator codeGenerator) throws CompiException {
        codeGenerator.gen("PUSH 0");
    }
}
