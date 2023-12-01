package SecondSemantic.Semantic;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Generation.TagHandler;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.Nodes.DefaultsBlocks.BlockDefaultConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ConcreteClass{
    public Token name;
    Token implementsName;
    Token extendsName;
    public HashMap<String, ConcreteMethod> methods;
    public List<ConcreteMethod> methodsInOrder = new ArrayList<>();
    public HashMap<String, ConcreteAttribute> attributes;
    public ConcreteMethod currentMethod;
    public SymbolTable symbolTable;
    public ConcreteMethod constructor;
    private boolean consolidated = false;
    public HashMap<String, ConcreteAttribute> hiddenAttributes = new HashMap<>();
    public int nextMethodOffset = 0;
    public int nextAttributeOffset = 1;

    public ConcreteClass(Token token, SymbolTable symbolTable) {
        this.name = token;
        this.implementsName = new Token("", "-", -1);
        this.extendsName = new Token("", "Object", -1);
        methods = new HashMap<String, ConcreteMethod>();
        attributes = new HashMap<String, ConcreteAttribute>();
        this.symbolTable = symbolTable;
    }

    public void addConstructor(ConcreteMethod m) throws SemanticException {
        if (m.name.getLexeme().equals(name.getLexeme())){
            if (constructor == null){
                constructor = m;
            } else
                symbolTable.semExceptionHandler.show(new SemanticException(m.name,"Constructor already defined in line "+ m.name.getRow()));
        } else
            symbolTable.semExceptionHandler.show(new SemanticException(m.name,"Constructor name must be the same as the class name in line "+ m.name.getRow()));
    }

    public void addMethod(ConcreteMethod m) throws SemanticException {
        if (!methods.containsKey(m.name.getLexeme())) {
            m.setOriginalClass(this);
            methods.put(m.name.getLexeme(), m);
        } else {
            methods.remove(m.name.getLexeme());
            symbolTable.semExceptionHandler.show(new SemanticException(m.name, "Method " + m.name.getLexeme() + " already defined in line " + m.name.getRow()));
        }
    }

    public void addAttribute(ConcreteAttribute a) throws SemanticException {
        if (!attributes.containsKey(a.name.getLexeme()))
            if (a.type.getLexeme().equals("void")){
                symbolTable.semExceptionHandler.show(new SemanticException(a.name,"Attribute " + a.name.getLexeme() + " cannot be void in line "+ a.name.getRow()));
            }else {
                a.setOriginalClass(this);
                attributes.put(a.name.getLexeme(), a);
            }
        else{
            attributes.remove(a.name.getLexeme());
            symbolTable.semExceptionHandler.show(new SemanticException(a.name,"Attribute " + a.name.getLexeme() + " already defined in line "+ a.name.getRow()));
        }
        a.variableType = ConcreteAttribute.ATTRIBUTE;
    }

    public void check() throws SemanticException {

        if (!Objects.equals(extendsName.getLexeme(), "$") && !symbolTable.classes.containsKey(extendsName.getLexeme()) && !symbolTable.interfaces.containsKey(extendsName.getLexeme()))
            symbolTable.semExceptionHandler.show(new SemanticException(extendsName,"Class extended " + extendsName.getLexeme() + " not defined in line "+ extendsName.getRow()));

        else {
            //now we will check 2 things:
            //1. if the concrete class is a class, then only can extend a class and implement interfaces
            //2. if the concrete class is an interface, then only can extend interfaces
            if (symbolTable.classes.containsKey(name.getLexeme())) {
                if (!Objects.equals(extendsName.getLexeme(), "$") && symbolTable.interfaces.containsKey(extendsName.getLexeme()))
                    symbolTable.semExceptionHandler.show(new SemanticException(extendsName,"Class " + name.getLexeme() + " cannot extend interface " + extendsName.getLexeme() + " in line "+ extendsName.getRow()));
                if (!Objects.equals(implementsName.getLexeme(), "-") && symbolTable.classes.containsKey(implementsName.getLexeme()))
                    symbolTable.semExceptionHandler.show(new SemanticException(implementsName,"Class " + name.getLexeme() + " cannot implement class " + implementsName.getLexeme() + " in line "+ implementsName.getRow()));
            } else {
                if (!Objects.equals(extendsName.getLexeme(), "$") && symbolTable.classes.containsKey(extendsName.getLexeme()))
                    symbolTable.semExceptionHandler.show(new SemanticException(extendsName,"Interface " + name.getLexeme() + " cannot extend class " + extendsName.getLexeme() + " in line "+ extendsName.getRow()));
            }


            for (ConcreteMethod m : methods.values()){

                if (m.type.getName().equals("idClass")) {
                    if (!symbolTable.classes.containsKey(m.type.getLexeme()) && !symbolTable.interfaces.containsKey(m.type.getLexeme())){
                        symbolTable.semExceptionHandler.show(new SemanticException(m.type, "Class or interface " + m.type.getLexeme() + " not defined in line " + m.type.getRow()));
                        break;
                    }

                }

                m.check();
            }

            for (ConcreteAttribute a : attributes.values()){
                if (a.type.getName().equals("idClass")) {
                    if (!symbolTable.classes.containsKey(a.type.getLexeme()) && !symbolTable.interfaces.containsKey(a.type.getLexeme()))
                        symbolTable.semExceptionHandler.show(new SemanticException(a.type,"Class or interface " + a.type.getLexeme() + " not defined in line "+ a.type.getRow()));
                }
            }
        }
    }

    public void consolidate(ArrayList parentsList) throws SemanticException {
        if (consolidated)
            return;
        //if im in the list, there is a cycle then symbolTable.semExceptionHandler.show(exception
        ConcreteClass parent = null;
        if (parentsList.contains(name.getLexeme()))
            symbolTable.semExceptionHandler.show(new SemanticException(name,"Cycle detected in class " + name.getLexeme() + " in line "+ name.getRow()));
        else {
            if (!consolidated){
            if (extendsName.getLexeme().equals("$")){}
            else if (extendsName.getLexeme().equals("Object")) inherit(extendsName.getLexeme());
            else {
                parent = symbolTable.classes.get(extendsName.getLexeme());
                if (parent == null)
                    parent = symbolTable.interfaces.get(extendsName.getLexeme());
                if (parent == null)
                    symbolTable.semExceptionHandler.show(new SemanticException(extendsName,"Class or interface " + extendsName.getLexeme() + " not defined in line "+ extendsName.getRow()));
                else {
                    parentsList.add(name.getLexeme());
                    parent.consolidate(parentsList);
                    inherit(extendsName.getLexeme());
                }
            }
            consolidated = true;
        }
        if (constructor == null){
            constructor = new ConcreteMethod(name, name, new Token("", "-", -1), symbolTable);
            constructor.methodBlock = new BlockDefaultConstructor(this, constructor);
        }
        checkCorrectInheritance();
        }

        if(implementsName.getLexeme().equals("-")){
            if (!extendsName.getLexeme().equals("$")){
                ConcreteClass parent2 = symbolTable.classes.get(extendsName.getLexeme());
                if (parent2 == null)
                    parent2 = symbolTable.interfaces.get(extendsName.getLexeme());
                nextMethodOffset = parent2.getNextMethodOffset();
            }
        } else {
            ConcreteClass implementedInterface = symbolTable.interfaces.get(implementsName.getLexeme());
            nextMethodOffset = implementedInterface.getNextMethodOffset();
        }

        if (symbolTable.classes.containsKey(name.getLexeme()))
            System.out.println("Offsets for class " + name.getLexeme() + ": ");
        else
            System.out.println("Offsets for interface " + name.getLexeme() + ": ");
        for(ConcreteMethod method : methods.values()) {
            boolean isStatic = method.isStatic.getLexeme().equals("static");
            boolean isRedefined = method.isRedefined();
            boolean isInherited = method.originalClass != this;
            boolean isDeclaredInInterface = false;
            if (!implementsName.getLexeme().equals("-")){
                ConcreteClass interfaceToImpl = symbolTable.interfaces.get(implementsName.getLexeme());
                if (interfaceToImpl.methods.containsKey(method.name.getLexeme())){
                    System.out.println("Method is declared in interface " + implementsName.getLexeme());
                    isDeclaredInInterface = true;
                }
            }

            boolean needsOffset = !(isStatic || isRedefined || isInherited);

            if (isDeclaredInInterface){
                System.out.println("Method declared in Interface: " + method.name.getLexeme() + " Offset: " + nextMethodOffset);
                ConcreteMethod methodInInterface = symbolTable.interfaces.get(implementsName.getLexeme()).methods.get(method.name.getLexeme());
                method.setOffset(methodInInterface.getOffset());
            } else if(needsOffset) {
                System.out.println("Method: " + method.name.getLexeme() + " Offset: " + nextMethodOffset);
                method.setOffset(nextMethodOffset);
                nextMethodOffset++;
            }
        }

        if (parent != null){
            nextAttributeOffset = parent.getNextAttributeOffset();
        }

        System.out.println("///Offsets for attribute in class " + name.getLexeme() + ": ");
        for(ConcreteAttribute attribute : attributes.values()) {
            boolean isStatic = attribute.isStatic.getLexeme().equals("static");
            boolean isInherited = attribute.originalClass != this;

            boolean needsOffset = !(isStatic || isInherited);

            if(needsOffset) {
                attribute.setOffset(nextAttributeOffset);
                nextAttributeOffset++;
            }

            System.out.println("Attribute: " + attribute.name.getLexeme() + " Offset: " + attribute.getOffset() + " declared in class " + attribute.originalClass.name.getLexeme());
        }

    }

    private int getNextAttributeOffset() {
        return nextAttributeOffset;
    }

    private int getNextMethodOffset() {
        return nextMethodOffset;
    }

    private void checkCorrectInheritance() throws SemanticException {
        //check if all methods from the interfaces are implemented
        if (!implementsName.getLexeme().equals("-")){
            ConcreteClass parent = symbolTable.interfaces.get(implementsName.getLexeme());
            if (parent == null)
                symbolTable.semExceptionHandler.show(new SemanticException(implementsName,"Interface " + implementsName.getLexeme() + " not defined in line "+ implementsName.getRow()));
            else
                for (ConcreteMethod m : parent.methods.values()){
                    if (!methods.containsKey(m.name.getLexeme()))
                        symbolTable.semExceptionHandler.show(new SemanticException(m.name,"Method " + m.name.getLexeme() + " from interface " + implementsName.getLexeme() + " not implemented in line "+ m.name.getRow()));
                    else{
                        //check if method is overriden, if the signature is the same, same type of return and same type and order of parameters then its ok
                        ConcreteMethod current = methods.get(m.name.getLexeme());
                        if (!current.type.getLexeme().equals(m.type.getLexeme()))
                            symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " from interface " + implementsName.getLexeme() + " not implemented in line "+ m.name.getRow()));
                        else if (current.parameters.size() != m.parameters.size())
                            symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " from interface " + implementsName.getLexeme() + " not implemented in line "+ m.name.getRow()));
                        //check if parameters are the same with the same order using parametersInOrder
                        else for (int i = 0; i < current.parametersInOrder.size(); i++){
                            if (!current.parametersInOrder.get(i).type.getLexeme().equals(m.parametersInOrder.get(i).type.getLexeme()))
                                symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " from interface " + implementsName.getLexeme() + " not implemented in line "+ m.name.getRow()));
                        }
                    }
                }
        }
    }

    private void inherit(String lexeme) throws SemanticException {
        ConcreteClass parent = symbolTable.classes.get(lexeme);
        if (parent == null)
            parent = symbolTable.interfaces.get(lexeme);
        if (parent == null)
            symbolTable.semExceptionHandler.show(new SemanticException(extendsName,"Class or interface " + extendsName.getLexeme() + " not defined in line "+ extendsName.getRow()));
        else{
            for (ConcreteAttribute a : parent.attributes.values()){
                if (!attributes.containsKey(a.name.getLexeme()))
                    attributes.put(a.name.getLexeme(), a);
                else{
                    //attributes overrides the parent's attributes but the parent's attributes are still there
                    hiddenAttributes.put(a.name.getLexeme(), a);
                }
            }

            for (ConcreteMethod m : parent.methods.values()){
                if (!methods.containsKey(m.name.getLexeme()))
                    methods.put(m.name.getLexeme(), m);
                else{
                    //check if method is override, if the signature is the same, same type of return and same type and order of parameters then it's ok
                    ConcreteMethod current = methods.get(m.name.getLexeme());
                    //check if method has the same static modifier
                    if (!current.isStatic.getLexeme().equals(m.isStatic.getLexeme()))
                        symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " need to be the same of the method declared in the parent "+ parent.name.getLexeme() + " in line "+ m.name.getRow() + " (static or not)"));
                    if (!current.type.getLexeme().equals(m.type.getLexeme()))
                        symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " already defined in the parent class "+ parent.name.getLexeme() + " in line "+ m.name.getRow() + " (return type)"));
                    else if (current.parameters.size() != m.parameters.size())
                        symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " already defined in the parent class "+ parent.name.getLexeme() + " in line "+ m.name.getRow() + " (number of parameters)"));
                    //check if parameters are the same with the same order using parametersInOrder
                    else for (int i = 0; i < current.parametersInOrder.size(); i++){
                        if (!current.parametersInOrder.get(i).type.getLexeme().equals(m.parametersInOrder.get(i).type.getLexeme()))
                            symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " already defined in the parent class "+ parent.name.getLexeme() + " in line "+ m.name.getRow() + " (type of parameters)"));
                    }
                    methods.get(m.name.getLexeme()).isRedefined = true;
                }
            }

            //first we copy the methods in order from the parent
            methodsInOrder.addAll(parent.methodsInOrder);
            //now we add the methods from the current class
            //if m is redefined then we need to replace the method in the list with the new one on the same position
            System.out.println("=============\n");
            for (ConcreteMethod m : methods.values()){
                if (!methodsInOrder.contains(m)) {
                    if (!m.isStatic.getLexeme().equals("static")){
                        if (!m.isRedefined){
                            System.out.println("Adding method " + m.name.getLexeme() + " to the list of methods in order");
                            methodsInOrder.add(m);
                        } else {
                            System.out.println("Replacing method " + m.name.getLexeme() + " in the list of methods in order");
                            ConcreteMethod toReplace = null;
                            for (ConcreteMethod m2 : methodsInOrder){
                                if (m2.name.getLexeme().equals(m.name.getLexeme())){
                                    toReplace = m2;
                                    break;
                                }
                            }
                            int index = methodsInOrder.indexOf(toReplace);
                            methodsInOrder.remove(index);
                            methodsInOrder.add(index, m);
                        }
                    }
                }
            }
            System.out.println("--- Methods in order: " + methodsInOrder.size() + " for class " + name.getLexeme());
            for (ConcreteMethod m : methodsInOrder){
                System.out.print("Method: " + m.name.getLexeme() + " declared in the class " + m.originalClass.name.getLexeme() + " has offset " + m.getOffset());
                System.out.println("");
            }
            System.out.println("=============");
        }
    }

    public void setImplementsName(Token implementsName) {
        this.implementsName = implementsName;
    }

    public void setExtendsName(Token extendsName) {
        this.extendsName = extendsName;
    }

    public void checkNamesAndTypes() throws SemanticException {
        //check constructor
        constructor.checkNamesAndTypes();
        for (ConcreteMethod m : methods.values()){
            if (m.originalClass == this || !m.name.getLexeme().equals("debugPrint"))
                m.checkNamesAndTypes();
        }
    }

    public boolean isSubTypeOf(ConcreteClass concreteClass) {
        if (concreteClass == null)
            return false;
        if (name.getLexeme().equals(concreteClass.name.getLexeme()) || implementsName.getLexeme().equals(concreteClass.name.getLexeme()))
            return true;
        else if (extendsName.getLexeme().equals("$"))
            return false;
        else if (extendsName.getLexeme().equals(concreteClass.name.getLexeme()))
            return true;
        else {
            ConcreteClass parent = symbolTable.classes.get(extendsName.getLexeme());
            if (parent == null)
                parent = symbolTable.interfaces.get(extendsName.getLexeme());
            if (parent == null)
                return false;
            else
                return parent.isSubTypeOf(concreteClass);
        }
    }

    public void generate(CodeGenerator codeGenerator) throws CompiException {
        generateVT(codeGenerator);
        generateAttr(codeGenerator);
        generateMethods(codeGenerator);
        generateConstructor(codeGenerator);
    }

    private void generateVT(CodeGenerator codeGenerator) throws CompiException {
        int numberOfMethods = methodsInOrder.size();
        int numberOfDynamicMethods = 0;
        String[] tagsInOrder = new String[numberOfMethods];
        System.out.println("Generating VTable for class " + name.getLexeme() + " with " + numberOfMethods + " methods");
        for(ConcreteMethod m : methodsInOrder) {
            System.out.println("Method: " + m.name.getLexeme() + " is static: " + m.isStatic.getLexeme() + " offset: " + m.getOffset());
            if(!m.isStatic.getLexeme().equals("static")) {
                int offset = m.getOffset();
                tagsInOrder[offset] = TagHandler.getMethodTag(m);
                numberOfDynamicMethods++;
            }
        }

        String tag = TagHandler.getVTableTag(this);
        StringBuilder sb = new StringBuilder(tag);

        if(numberOfDynamicMethods > 0) {
            sb.append(": DW ");
            for (String s : tagsInOrder) {
                if (s != null) {
                    sb.append(s);
                    sb.append(",");
                }
            }
            int lastIndexOfComma = sb.lastIndexOf(",");
            sb.deleteCharAt(lastIndexOfComma);
        } else {
            String cNop = " # This class doesnt have any dynamic methods";
            sb.append(": NOP");
            sb.append(cNop);
        }

        String instruction = sb.toString();

        codeGenerator.gen(".DATA");
        codeGenerator.gen(instruction);
        System.out.println("--- Generating VTable for class " + name.getLexeme() + " with tag " + tag);
        System.out.println("VTable: " + instruction + "\n");
    }

    private void generateAttr(CodeGenerator codeGenerator) throws CompiException {
        List<ConcreteAttribute> staticAttributes = new ArrayList<>();

        for(ConcreteAttribute attribute : attributes.values()) {
            if(attribute.isStatic().getName().equals("keyword_static")) {
                System.out.println(" ======= AGREGUE ATRIBUTO ESTATICO " + attribute.getName().getLexeme() + " EN LA CLASE " + name.getLexeme());
                staticAttributes.add(attribute);
            }
        }

        //if it has static attributes, we need to create a new section in the .DATA segment
        if(!staticAttributes.isEmpty())
            codeGenerator.gen(".DATA");

        for(ConcreteAttribute attribute : staticAttributes) {
            if (attribute.getOriginalClass() == this){
                String tag = TagHandler.getAttributeTag(attribute,this);
                String instruction = tag + ": DW 0";
                System.out.println("--- Generating attribute " + attribute.getName().getLexeme() + " in the class " + name.getLexeme() + " with tag " + tag);
                codeGenerator.gen(instruction);
            }
        }
    }

    private void generateMethods(CodeGenerator codeGenerator) throws CompiException {
        if(methodsInOrder.size() > 0)
            codeGenerator.gen(".CODE");

        for(ConcreteMethod m : methodsInOrder) {
            if (m.getOriginalClass() == this)
                m.generateMethod(codeGenerator);
        }

        for(ConcreteMethod m : methods.values()) {
            if (m.getOriginalClass() == this && m.isStatic.getLexeme().equals("static"))
                m.generateMethod(codeGenerator);
        }
    }

    private void generateConstructor(CodeGenerator codeGenerator) throws CompiException {
        constructor.generateConstructor(codeGenerator);
    }

    public void assignOffset(CodeGenerator codeGenerator) {
        int nextOffset = 0;
        if(implementsName.getLexeme().equals("-")){
            nextOffset = 0;
        } else {
            ConcreteClass implementedInterface = symbolTable.interfaces.get(implementsName.getLexeme());
            nextOffset = implementedInterface.getNextMethodOffset();
        }
        System.out.println(" (((((((( INITIAL OFFSET FOR INTERFACE " + name.getLexeme() + " IS " + nextOffset + " ))))))))");

        for (ConcreteMethod m : methods.values()){
            boolean isRedefined = m.isRedefined();
            boolean isInherited = m.originalClass != this;
            boolean needsOffset = !(isRedefined || isInherited);

            if(needsOffset) {
                m.setOffset(nextOffset);
                nextOffset++;
            }
        }

        nextMethodOffset = nextOffset;
    }
}

interface A {
    int a1();
}

class AImpl implements A {
    public int a1() {
        return 10;
    }
}

class BImpl implements A {
    public int a1() {
        return 20;
    }
}