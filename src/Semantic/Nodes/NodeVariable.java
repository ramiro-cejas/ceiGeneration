package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Generation.TagHandler;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.*;

import java.util.ArrayList;

public class NodeVariable implements Node{

    public Token name;
    public NodeBlock parentBlock;
    private boolean alreadyChecked = false;
    public Token type;
    public NodeVariable childChain, parentChain;
    public boolean isMethod = false;
    public ArrayList<Node> parameters = new ArrayList<>();
    private ConcreteMethod methodToCall;
    private ConcreteAttribute referencedVariable;

    public NodeVariable(Token name, NodeBlock parentBlock) {
        this.name = name;
        this.parentBlock = parentBlock;
    }

    public NodeVariable(){}

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (!alreadyChecked){
            //check parameters
            for (Node parameter : parameters){
                parameter.check(symbolTable);
            }
            if (isMethod){
                if (parentChain == null){
                    ConcreteClass currentClass = parentBlock.currentClass;
                    ConcreteMethod methodToMatch = currentClass.methods.get(name.getLexeme());
                    if (parentBlock.currentMethod.isStatic.getLexeme().equals("static") && !methodToMatch.isStatic.getLexeme().equals("static")){
                        if (!(parentChain instanceof NodeVariableConstructor))
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Static method " + name.getLexeme() + " cannot be called from a non static method"));
                    }
                    if (methodToMatch == null){
                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + currentClass.name.getLexeme()));
                    } else {
                        if (methodToMatch.parameters.size() != parameters.size()){
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + currentClass.name.getLexeme() + " with the given parameters"));
                        } else {
                            for (int i = 0; i < parameters.size(); i++){
                                if (!parameters.get(i).getType().getLexeme().equals(methodToMatch.parametersInOrder.get(i).getType().getLexeme())){
                                    symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + currentClass.name.getLexeme() + " with the given parameters"));
                                }
                            }
                            type = methodToMatch.type;
                        }
                    }
                    methodToCall = methodToMatch;
                } else {
                    ConcreteMethod methodToMatch = null;
                    //im the chain of something
                    if (parentChain.getType().getName().equals("idClass")) {
                        if (symbolTable.classes.containsKey(parentChain.getType().getLexeme())){
                            methodToMatch = symbolTable.classes.get(parentChain.getType().getLexeme()).methods.get(name.getLexeme());
                            if (parentChain.getType().getLexeme().equals(parentBlock.currentClass.name.getLexeme()))
                                if (parentBlock.currentMethod.isStatic.getLexeme().equals("static") && !methodToMatch.isStatic.getLexeme().equals("static")){
                                    if (!(parentChain instanceof NodeVariableConstructor))
                                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Static method " + name.getLexeme() + " cannot be called from a non static method"));
                                }
                            if (methodToMatch == null){
                                symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + parentChain.getType().getLexeme()));
                            } else {
                                if (methodToMatch.parameters.size() != parameters.size()){
                                    symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + parentChain.getType().getLexeme() + " with the given parameters"));
                                } else {
                                    for (int i = 0; i < parameters.size(); i++){
                                        if (!parameters.get(i).getType().getLexeme().equals(methodToMatch.parametersInOrder.get(i).getType().getLexeme())){
                                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + parentChain.getType().getLexeme() + " with the given parameters"));
                                        }
                                    }
                                    type = methodToMatch.type;
                                }
                            }
                        } else {
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + parentChain.getType().getLexeme()));
                        }
                    } else {
                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined"));
                    }
                    methodToCall = methodToMatch;

                }
            } else {
                //then is access to an attribute
                if (parentChain == null){
                    //search in the block only for class attribute
                    ConcreteAttribute toCheck = parentBlock.currentClass.attributes.get(name.getLexeme());
                    if (toCheck != null){
                        if (parentBlock.currentMethod.isStatic.getLexeme().equals("static") && toCheck.isStatic.getLexeme().equals("-"))
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Non static attribute " + name.getLexeme() + " cannot be called from a static method"));
                    }
                    if (parentBlock.getVisible(name.getLexeme()) != toCheck){
                        toCheck = parentBlock.getVisible(name.getLexeme());
                    }

                    if (toCheck == null){
                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined"));
                    } else {
                        type = toCheck.getType();
                    }
                    referencedVariable = toCheck;
                } else {
                    //im the chain of something
                    ConcreteAttribute attributeToMatch = null;
                    if (parentChain.getType().getName().equals("idClass")) {
                        if (symbolTable.classes.containsKey(parentChain.getType().getLexeme())){
                            attributeToMatch = symbolTable.classes.get(parentChain.getType().getLexeme()).attributes.get(name.getLexeme());
                            if (attributeToMatch == null){
                                symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined in class " + parentChain.getType().getLexeme()));
                            } else {
                                if (parentChain.getType().getLexeme().equals(parentBlock.currentClass.name.getLexeme()))
                                    if (parentBlock.currentMethod.isStatic.getLexeme().equals("static") && !attributeToMatch.isStatic.getLexeme().equals("static")){
                                        if (!(parentChain instanceof NodeVariableConstructor))
                                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Non static attribute " + name.getLexeme() + " cannot be called from a static method"));
                                    }
                                type = attributeToMatch.getType();
                            }
                        } else {
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined in class " + parentChain.getType().getLexeme()));
                        }
                    } else {
                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined"));
                    }
                    referencedVariable = attributeToMatch;
                }
            }

            //then check the child chain
            if (childChain != null){
                childChain.check(symbolTable);
                type = childChain.type;
            }
            alreadyChecked = true;
        }
    }

    @Override
    public Token getType() {
        return type;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        parentBlock = nodeBlock;
    }

    @Override
    public Token getToken() {
        return name;
    }

    public void setChildChain(NodeVariable nodeVariable){
        childChain = nodeVariable;
        childChain.setParentChain(this);
    }

    public void setParentChain(NodeVariable nodeVariable){
        parentChain = nodeVariable;
    }

    public boolean inTheLastIsMethod(){
        if (childChain == null)
            return isMethod;
        else
            return childChain.inTheLastIsMethod();
    }

    @Override
    public void generate(CodeGenerator codeGenerator) throws CompiException {
        if (isMethod){
            //si el metodo referenciado es estatico
            if(methodToCall.isStatic.getLexeme().equals("static")) {
                System.out.println("Generating static method access " + name.getLexeme() + " in the class " + parentBlock.currentClass.name.getLexeme() + " with type " + type.getLexeme());
                generateStaticCall(codeGenerator);
            } else {
                System.out.println("Generating non static method access " + name.getLexeme() + " in the class " + parentBlock.currentClass.name.getLexeme() + " with type " + type.getLexeme());
                generateDynamicCall(codeGenerator);
            }
        } else {
            if(parentBlock.getVisible(name.getLexeme()).isStatic.getLexeme().equals("static")) {
                generateStaticAttr(codeGenerator);
            } else {
                generateDynamicAttr(codeGenerator);
            }
        }
    }

    protected void generateStaticAttr(CodeGenerator codeGenerator) throws CompiException {
        System.out.println("Generating static attribute access " + name.getLexeme() + " in the class " + parentBlock.currentClass.name.getLexeme() + " with type " + type.getLexeme());
        boolean readAccess = inTheLastIsMethod() || childChain != null;
        ConcreteAttribute attribute = referencedVariable;
        String tag = TagHandler.getAttributeTag(attribute, parentBlock.currentClass);

        String cPush = " # We put the static attribute's tag at the top of the stack";
        codeGenerator.gen("PUSH " + tag + cPush);

        if(readAccess) {
            String cRead = " # We read the attributes value from the data using the tag";
            codeGenerator.gen("LOADREF 0" + cRead);
        } else {
            String cSwap = " # We swap the tag and what we desire to write into the attribute";
            codeGenerator.gen("SWAP" + cSwap);

            String cWrite = " # We write into the data through the tag";
            codeGenerator.gen("STOREREF 0" + cWrite);
        }
    }

    protected void generateDynamicAttr(CodeGenerator codeGenerator) throws CompiException {
        System.out.println("Generating dynamic attribute access " + name.getLexeme() + " in the class " + parentBlock.currentClass.name.getLexeme() + " with type " + type.getLexeme());
        //if the referenced variable is a class attribute
        boolean attribute = parentBlock.classAttributes.contains(referencedVariable);
        boolean readAccess = inTheLastIsMethod() || childChain != null;
        int offset = referencedVariable.getOffset();
        //boolean isParameter = parentBlock.methodParameters.contains(referencedVariable);

        if(attribute) {
            String cThis = " # Accessing a dynamic attribute, we put a reference to 'this' at the top of the stack.";
            codeGenerator.gen("LOAD 3" + cThis);

            if(readAccess) {
                String cRead = " # We get the variable from the heap through the 'this' reference and its offset";
                codeGenerator.gen("LOADREF " + offset + cRead);
            } else {
                String cSwap = " # We swap the 'this' reference and what we desire to write into the attribute";
                codeGenerator.gen("SWAP" + cSwap);

                String cWrite = " # We write into the heap through the 'this' reference and the offset";
                codeGenerator.gen("STOREREF " + offset + cWrite);
            }
        } else {
            if(readAccess) {
                System.out.println("Generating read acces to local variable or parameter " + name.getLexeme() + " in the class " + parentBlock.currentClass.name.getLexeme() + " with type " + type.getLexeme());
                String cLoad = " # We get the variable from the stack through its offset";
                codeGenerator.gen("LOAD " + offset + cLoad);
            } else {
                String cStore = " # We write into the variable in the stack, through its offset";
                codeGenerator.gen("STORE " + offset + cStore);
            }
        }


    }

    private void generateStaticCall(CodeGenerator codeGenerator) throws CompiException {
        String cPop = " # We consume the 'this' reference from the 'chainee' that's on top of the stack, since we won't use it";
        codeGenerator.gen("POP" + cPop);

        if(!(parentBlock.currentMethod.type.getLexeme().equals("void"))) {
            String cRet = " # We reserve a memory cell for the method's return value";
            codeGenerator.gen("RMEM 1" + cRet);
        }

        for(Node argument : parameters) {
            argument.generate(codeGenerator);
        }

        String tag = "Method_" + name.getLexeme() + "@" + methodToCall.originalClass.name.getLexeme();
        String cTag = " # We push the static method's tag to the top of the stack";
        codeGenerator.gen("PUSH " + tag + cTag);

        String cCall = " # We make the call.";
        codeGenerator.gen("CALL" + cCall);
    }

    private void generateDynamicCall(CodeGenerator codeGenerator) throws CompiException {
        String cSwap = " # We swap to keep the 'this' reference at the top of the stack";

        if(!(parentBlock.currentMethod.type.getLexeme().equals("void"))) {
            String cRet = " # We reserve a memory cell for the method's return value";
            codeGenerator.gen("RMEM 1" + cRet);

            codeGenerator.gen("SWAP " + cSwap);
        }

        for(Node argument : parameters) {
            argument.generate(codeGenerator);
            codeGenerator.gen("SWAP " + cSwap);
        }

        String cDup = " # We duplicate the 'this' reference so we don't lose it when it's used by LOADREF";
        codeGenerator.gen("DUP" + cDup);

        String cLoad1 = " # We load the VTable (VTable offset is always 0)";
        codeGenerator.gen("LOADREF 0" + cLoad1);

        String cLoad2 = " # We load the method's address through the VTable.";
        codeGenerator.gen("LOADREF " + methodToCall.getOffset() + cLoad2);

        String cCall = " # We make the call.";
        codeGenerator.gen("CALL" + cCall);
    }

}
