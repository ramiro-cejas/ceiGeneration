package SecondSemantic.Semantic;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Generation.TagHandler;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.Nodes.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ConcreteMethod {

    public Token name;
    public Token isStatic;
    public Token type;
    public HashMap<String, ConcreteAttribute> parameters;
    SymbolTable symbolTable;
    public ArrayList<ConcreteAttribute> parametersInOrder;
    private boolean alreadyChecked = false;
    public NodeBlock currentBlock;
    public NodeBlock methodBlock;
    public ConcreteClass originalClass;
    public int offset;
    public boolean isRedefined = false;

    public ConcreteMethod(Token name, Token type, Token isStatic, SymbolTable symbolTable) {
        this.name = name;
        this.type = type;
        this.isStatic = isStatic;
        this.symbolTable = symbolTable;
        parameters = new HashMap<>();
        parametersInOrder = new ArrayList<>();
    }

    public void addParameter(ConcreteAttribute p) throws SemanticException {
        if (!parameters.containsKey(p.name.getLexeme())){
            if (p.type.getLexeme().equals("void")){
                symbolTable.semExceptionHandler.show(new SemanticException(p.name,"Parameter " + p.name.getLexeme() + " cannot be void in line "+ p.name.getRow()));
            }else {
                parametersInOrder.add(p);
                parameters.put(p.name.getLexeme(), p);
            }
        }
        else
            symbolTable.semExceptionHandler.show(new SemanticException(p.name,"Parameter " + p.name.getLexeme() + " already defined in line "+ p.name.getRow()));
        p.variableType = ConcreteAttribute.PARAMETER;
    }

    public void check() throws SemanticException {
        if (!alreadyChecked){
            for (ConcreteAttribute p : parameters.values()){
                if (p.type.getName().equals("idClass")) {
                    if (!symbolTable.classes.containsKey(p.type.getLexeme()) && !symbolTable.interfaces.containsKey(p.type.getLexeme()))
                        symbolTable.semExceptionHandler.show(new SemanticException(p.type,"Class or interface " + p.type.getLexeme() + " not defined in line "+ p.type.getRow()));
                } else if (p.type.getLexeme().equals("void")){
                    symbolTable.semExceptionHandler.show(new SemanticException(p.name,"Parameter " + p.name.getLexeme() + " cannot be void in line "+ p.name.getRow()));
                }
            }
            alreadyChecked = true;
        }
    }

    public void checkNamesAndTypes() throws SemanticException {
        if (methodBlock != null){
            methodBlock.check(symbolTable);
        }
    }

    public void generateMethod(CodeGenerator codeGenerator) throws CompiException {
        // assign offsets to the parameters
        int staticBonus = isStatic.getName().equals("keyword_static") ? 0 : 1;
        int numberOfParameters = parametersInOrder.size();
        int minOffset = 3 + staticBonus;
        for(int i = 1; i <= numberOfParameters; i++) {
            int offset = numberOfParameters - i + minOffset;
            int index = i-1;
            parametersInOrder.get(index).setOffset(offset);
        }

        String tag = TagHandler.getMethodTag(this);
        generate(codeGenerator, tag);
    }

    public void generateConstructor(CodeGenerator codeGenerator) throws CompiException {
        // assign offsets to the parameters
        int staticBonus = isStatic.getName().equals("keyword_static") ? 0 : 1;
        int numberOfParameters = parametersInOrder.size();
        int minOffset = 3 + staticBonus;
        for(int i = 1; i <= numberOfParameters; i++) {
            int offset = numberOfParameters - i + minOffset;
            int index = i-1;
            parametersInOrder.get(index).setOffset(offset);
        }

        String tag = TagHandler.getConstructorTag(this);
        generate(codeGenerator, tag);
    }

    private void generate(CodeGenerator codeGenerator, String tag) throws CompiException {
        codeGenerator.gen("");
        codeGenerator.gen(".CODE");

        String c1 = " # We store the dynamic link";
        codeGenerator.gen(tag + ": LOADFP" + c1);

        String c2 = " # We signal were the AR starts";
        codeGenerator.gen("LOADSP" + c2);

        String c3 = " # We signal the AR being built as the current AR";
        codeGenerator.gen("STOREFP" + c3);

        //first we reserve space for the arguments
        //codeGenerator.gen("RMEM " + parameters.size() + " # We reserve space for the arguments");
        //here we will retrieve the parameters from the stack in reverse order
        /*for (int i = parametersInOrder.size() - 1; i >= 0; i--) {
            codeGenerator.gen("LOAD " + i + " # We load the parameter " + parametersInOrder.get(i).name.getLexeme() + " to the top of the stack");
        }*/

        methodBlock.methodParameters.clear();
        methodBlock.methodParameters.addAll(parametersInOrder);
        methodBlock.generate(codeGenerator);

        String c4 = " # We point FP to caller's AR";
        codeGenerator.gen("STOREFP" + c4);

        int dynamicExtraCell = isStatic.getLexeme().equals("static") ? 0 : 1;
        int n = parameters.size() + dynamicExtraCell;

        String c5 = " # We free up memory cells equal to number of params [+1 if unit is dynamic]";
        codeGenerator.gen("RET " + n + c5);
    }

    public int getOffset() {
        return offset;
    }

    public void setOriginalClass(ConcreteClass concreteClass) {
        originalClass = concreteClass;
    }

    public ConcreteClass getOriginalClass() {
        return originalClass;
    }

    public String getTag() {
        return TagHandler.getMethodTag(this);
    }

    public boolean isRedefined() {
        return isRedefined;
    }

    public void setOffset(int nextMethodOffset) {
        this.offset = nextMethodOffset;
    }

}
