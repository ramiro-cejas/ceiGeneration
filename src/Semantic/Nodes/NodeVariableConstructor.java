package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Generation.TagHandler;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.*;

public class NodeVariableConstructor extends NodeVariable{

    private boolean alreadyChecked = false;
    private ConcreteClass classConstructed;

    public NodeVariableConstructor(Token name, NodeBlock parentBlock, Token type) {
        super(name, parentBlock);
        this.type = type;
    }

    public void check(SymbolTable symbolTable) throws SemanticException {
        if (alreadyChecked)
            return;
        for (Node parameter : parameters){
            parameter.check(symbolTable);
        }
        if (symbolTable.classes.containsKey(type.getLexeme())){
            if (symbolTable.classes.get(type.getLexeme()).constructor == null){
                symbolTable.semExceptionHandler.show(new SemanticException(type,"Class " + type.getLexeme() + " has no constructor"));
            } else {
                if (symbolTable.classes.get(type.getLexeme()).constructor.parameters.size() != parameters.size()){
                    symbolTable.semExceptionHandler.show(new SemanticException(type,"Constructor of class " + type.getLexeme() + " has different number of parameters"));
                } else {
                    for (int i = 0; i < parameters.size(); i++){
                        //check if the parameter is a subtype of the parameter in the constructor
                        //if the parameter is a class, check if it's a subtype of the parameter in the constructor
                        if (parameters.get(i).getType().getName().equals("idClass")){
                            ConcreteClass classOfParameter = symbolTable.classes.get(parameters.get(i).getType().getLexeme());
                            if (classOfParameter == null){
                                classOfParameter = symbolTable.interfaces.get(parameters.get(i).getType().getLexeme());
                            }
                            ConcreteMethod constructor = symbolTable.classes.get(type.getLexeme()).constructor;
                            ConcreteClass classOfConstructorParameter = symbolTable.classes.get(constructor.parametersInOrder.get(i).getType().getLexeme());
                            if (classOfConstructorParameter == null){
                                classOfConstructorParameter = symbolTable.interfaces.get(constructor.parametersInOrder.get(i).getType().getLexeme());
                            }
                            if (!classOfParameter.isSubTypeOf(classOfConstructorParameter)){
                                symbolTable.semExceptionHandler.show(new SemanticException(type,"Parameter " + i + " is not a subtype of the parameter in the constructor"));
                            }
                        } else {
                            if (!parameters.get(i).getType().getLexeme().equals(symbolTable.classes.get(type.getLexeme()).constructor.parametersInOrder.get(i).getType().getLexeme())){
                                symbolTable.semExceptionHandler.show(new SemanticException(type,"Parameter " + i + " is not the same type as the parameter in the constructor"));
                            }
                        }
                    }
                }
            }
            if(childChain != null){
                childChain.check(symbolTable);
                type = childChain.type;
            }
        } else {
            symbolTable.semExceptionHandler.show(new SemanticException(type,"Type " + type.getLexeme() + " is not a defined class"));
        }
        alreadyChecked = true;
        classConstructed = symbolTable.classes.get(type.getLexeme());
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        System.out.println("Generating constructor access");

        String cReserve = " # We reserve a memory cell to store the pointer to the constructed object";
        codeGenerator.gen("RMEM 1" + cReserve);

        int instanceVariableCounter = 0;
        for(ConcreteAttribute attribute : classConstructed.attributes.values()) {
            if(!attribute.isStatic.getName().equals("keyword_static"))
                instanceVariableCounter++;
        }
        int vTableSpace = 1;
        int reservedCells;

        if(classConstructed.name.getLexeme().equals("String")) {
            //string doesnt have "true" attributes but we need to consider one for it.
            //note, this may be better done by giving it a private attribute.
            reservedCells = 1 + vTableSpace;
        } else {
            reservedCells = instanceVariableCounter + vTableSpace;
        }

        String c2 = " # We load malloc's parameter: the number of cells to reserve for the constructed object";
        codeGenerator.gen("PUSH " + reservedCells + c2);

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

        //Now, we must make the call to the constructor itself.

        String c8 = " # We duplicate malloc's return and we use it as the 'this' reference for the constructor";
        codeGenerator.gen("DUP" + c8);

        String c9 = " # We swap to keep the 'this' reference at the top of the stack";
        for(Node argument : parameters) {
            argument.generate(codeGenerator);
            codeGenerator.gen("SWAP " + c9);
        }

        ConcreteMethod constructor = classConstructed.constructor;
        String constructorTag = TagHandler.getConstructorTag(constructor);

        String c10 = " # We put the constructor's tag on top of the stack";
        codeGenerator.gen("PUSH " + constructorTag + c10);

        String c11 = " # We make the call";
        codeGenerator.gen("CALL" + c11);

        if (childChain != null){
            childChain.generate(codeGenerator);
        }
    }
}
