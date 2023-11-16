package SecondSemantic.Semantic;

import SecondSemantic.Extras.CompiException;
import SecondSemantic.Generation.CodeGenerator;
import SecondSemantic.Generation.TagHandler;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.Nodes.DefaultsBlocks.*;
import SecondSemantic.Semantic.Nodes.NodeBlock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SymbolTable {
    public HashMap<String, ConcreteClass> classes;
    public HashMap<String, ConcreteClass> interfaces;
    public ConcreteClass currentClass;
    ArrayList<SemanticException> errors;
    public SemExceptionHandler semExceptionHandler = new SemExceptionHandler(this);
    public CodeGenerator codeGenerator;
    public ConcreteMethod mainMethod;

    public SymbolTable() throws SemanticException {
        classes = new HashMap<>();
        interfaces = new HashMap<>();
        currentClass = null;
        addDefaultsClasses();
        errors = new ArrayList<>();
    }

    private void addDefaultsClasses() throws SemanticException {
        ConcreteClass object = new ConcreteClass(new Token("Object", "Object", -1), this);
        object.extendsName = (new Token("$","$",-1));
        addObjectMethods(object);
        ConcreteMethod objectConstructor = new ConcreteMethod(new Token("Object", "Object", -1), new Token("Object", "Object", -1), new Token("-", "-", -1), this);
        objectConstructor.methodBlock = new BlockObjectConstructor(object, objectConstructor);
        object.constructor = objectConstructor;
        addClass(object);

        ConcreteClass system = new ConcreteClass(new Token("System", "System", -1), this);
        addSystemMethods(system);
        ConcreteMethod systemConstructor = new ConcreteMethod(new Token("System", "System", -1), new Token("System", "System", -1), new Token("-", "-", -1), this);
        systemConstructor.methodBlock = new BlockSystemConstructor(system, systemConstructor);
        system.constructor = systemConstructor;
        addClass(system);

        ConcreteClass string = new ConcreteClass(new Token("String", "String", -1), this);
        ConcreteMethod stringConstructor = new ConcreteMethod(new Token("String", "String", -1), new Token("String", "String", -1), new Token("-", "-", -1), this);
        stringConstructor.methodBlock = new BlockStringConstructor(string, stringConstructor);
        string.constructor = stringConstructor;
        addClass(string);
    }

    private void addSystemMethods(ConcreteClass system) throws SemanticException {
        ConcreteMethod method = new ConcreteMethod(new Token("idMetVar", "read", -1), new Token("keyword_int", "int", -1), new Token("keyword_static", "static", -1), this);
        method.methodBlock = new BlockRead(system, method);
        system.addMethod(method);

        method = new ConcreteMethod(new Token("idMetVar", "printB", -1), new Token("keyword_void", "void", -1), new Token("keyword_static", "static", -1), this);
        method.addParameter(new ConcreteAttribute(new Token("idMetVar","b",-1), new Token("keyword_boolean","boolean",-1), new Token("-", "-", -1)));
        method.methodBlock = new BlockPrintB(system, method);
        system.addMethod(method);

        method = new ConcreteMethod(new Token("idMetVar", "printC", -1), new Token("keyword_void", "void", -1), new Token("keyword_static", "static", -1), this);
        method.addParameter(new ConcreteAttribute(new Token("idMetVar","c",-1), new Token("keyword_char","char",-1), new Token("-", "-", -1)));
        method.methodBlock = new BlockPrintC(system, method);
        system.addMethod(method);

        method = new ConcreteMethod(new Token("idMetVar", "printI", -1), new Token("keyword_void", "void", -1), new Token("keyword_static", "static", -1), this);
        method.addParameter(new ConcreteAttribute(new Token("idMetVar","i",-1), new Token("keyword_int","int",-1), new Token("-", "-", -1)));
        method.methodBlock = new BlockPrintI(system, method);
        system.addMethod(method);

        method = new ConcreteMethod(new Token("idMetVar", "printS", -1), new Token("keyword_void", "void", -1), new Token("keyword_static", "static", -1), this);
        method.addParameter(new ConcreteAttribute(new Token("idMetVar","s",-1), new Token("keyword_String","String",-1), new Token("-", "-", -1)));
        method.methodBlock = new BlockPrintS(system, method);
        system.addMethod(method);

        method = new ConcreteMethod(new Token("idMetVar", "println", -1), new Token("keyword_void", "void", -1), new Token("keyword_static", "static", -1), this);
        method.methodBlock = new BlockPrintln(system, method);
        system.addMethod(method);

        method = new ConcreteMethod(new Token("idMetVar", "printBln", -1), new Token("keyword_void", "void", -1), new Token("keyword_static", "static", -1), this);
        method.addParameter(new ConcreteAttribute(new Token("idMetVar","b",-1), new Token("keyword_boolean","boolean",-1), new Token("-", "-", -1)));
        method.methodBlock = new BlockPrintBln(system, method);
        system.addMethod(method);

        method = new ConcreteMethod(new Token("idMetVar", "printCln", -1), new Token("keyword_void", "void", -1), new Token("keyword_static", "static", -1), this);
        method.addParameter(new ConcreteAttribute(new Token("idMetVar","c",-1), new Token("keyword_char","char",-1), new Token("-", "-", -1)));
        method.methodBlock = new BlockPrintCln(system, method);
        system.addMethod(method);

        method = new ConcreteMethod(new Token("idMetVar", "printIln", -1), new Token("keyword_void", "void", -1), new Token("keyword_static", "static", -1), this);
        method.addParameter(new ConcreteAttribute(new Token("idMetVar","i",-1), new Token("keyword_int","int",-1), new Token("-", "-", -1)));
        method.methodBlock = new BlockPrintIln(system, method);
        system.addMethod(method);

        method = new ConcreteMethod(new Token("idMetVar", "printSln", -1), new Token("keyword_void", "void", -1), new Token("keyword_static", "static", -1), this);
        method.addParameter(new ConcreteAttribute(new Token("idMetVar","s",-1), new Token("keyword_String","String",-1), new Token("-", "-", -1)));
        method.methodBlock = new BlockPrintSln(system, method);
        system.addMethod(method);
    }

    private void addObjectMethods(ConcreteClass object) throws SemanticException {
        ConcreteMethod method = new ConcreteMethod(new Token("idMetVar", "debugPrint", -1), new Token("keyword_void", "void", -1), new Token("keyword_static", "static", -1), this);
        method.addParameter(new ConcreteAttribute(new Token("idMetVar","i",-1), new Token("keyword_int","int",-1), new Token("-", "-", -1)));
        method.methodBlock = new NodeBlock(new Token("punctuator_{","{",-1), object, method, null);
        object.addMethod(method);

        ConcreteMethod constructor = new ConcreteMethod(new Token("idMetVar", "Object", -1), new Token("idMetVar", "Object", -1), new Token("-", "-", -1), this);
        constructor.methodBlock = new NodeBlock(new Token("punctuator_{","{",-1), object, constructor, null);
        object.addConstructor(constructor);
    }

    public void addClass(ConcreteClass c) throws SemanticException {
        if (!classes.containsKey(c.name.getLexeme()) && !interfaces.containsKey(c.name.getLexeme())){
            classes.put(c.name.getLexeme(), c);
        }
        else {
            classes.remove(c.name.getLexeme());
            interfaces.remove(c.name.getLexeme());
            semExceptionHandler.show(new SemanticException(c.name, "Class " + c.name.getLexeme() + " already defined in line " + c.name.getRow()));
        }
    }

    public void addInterface(ConcreteClass c) throws SemanticException {
        if (!interfaces.containsKey(c.name.getLexeme()) && !classes.containsKey(c.name.getLexeme())){
            interfaces.put(c.name.getLexeme(), c);
        }
        else{
            classes.remove(c.name.getLexeme());
            interfaces.remove(c.name.getLexeme());

            semExceptionHandler.show(new SemanticException(c.name,"Interface " + c.name.getLexeme() + " already defined in line "+ c.name.getRow()));
        }
    }

    public void check() throws SemanticException {
        for (ConcreteClass c : interfaces.values()){
            c.check();
            c.consolidate(new ArrayList());
        }
        for (ConcreteClass c : classes.values()){
            c.check();
            c.consolidate(new ArrayList());
        }
        checkAtLeastOneMain();
        checkInterfaces();
        checkClasses();
    }

    private void checkClasses() throws SemanticException {
        for (ConcreteClass c : classes.values()){
            //if c extends other thing than a class then error
            if (!c.extendsName.getLexeme().equals("$")){
                if (!classes.containsKey(c.extendsName.getLexeme()))
                    semExceptionHandler.show(new SemanticException(c.extendsName, "Class " + c.name.getLexeme() + " cannot extend interface " + c.extendsName.getLexeme()));
            }
        }
    }

    private void checkInterfaces() throws SemanticException {
        for (ConcreteClass c : interfaces.values()){
            //if c extends other thing than a interface then error
            if (!c.extendsName.getLexeme().equals("$") && !c.implementsName.getLexeme().equals("-")){
                if (!interfaces.containsKey(c.extendsName.getLexeme()))
                    semExceptionHandler.show(new SemanticException(c.extendsName, "Interface " + c.name.getLexeme() + " cannot extend class " + c.extendsName.getLexeme()));
            }
            //if c implements other thing than a - then error
            if (!c.implementsName.getLexeme().equals("-")){
                semExceptionHandler.show(new SemanticException(c.implementsName, "Interface " + c.name.getLexeme() + " cannot implement " + c.implementsName.getLexeme()));
            }
            for (ConcreteMethod m : c.methods.values()){
                if (m.isStatic.getLexeme().equals("static"))
                    semExceptionHandler.show(new SemanticException(m.name, "Interface " + c.name.getLexeme() + " cannot have static methods"));
            }
        }
    }

    private void checkAtLeastOneMain() throws SemanticException {
        boolean hasMain = false;
        for (ConcreteClass c : classes.values()){
            for (ConcreteMethod m : c.methods.values()){
                if (m.name.getLexeme().equals("main") && m.type.getLexeme().equals("void") && m.parameters.size() == 0 && m.isStatic.getLexeme().equals("static")){
                    hasMain = true;
                    mainMethod = m;
                    break;
                }
            }
        }
        if (!hasMain)
            semExceptionHandler.show(new SemanticException(new Token("main", "main", -1), "No main method found"));
    }

    public String toString(){
        StringBuilder toReturn = new StringBuilder();

        for (ConcreteClass c : interfaces.values()){
            toReturn.append("Interface: ").append(c.name.getLexeme()).append("\n");
            if (!c.extendsName.getLexeme().equals("-"))
                toReturn.append("├─ Extends: ").append(c.extendsName.getLexeme()).append("\n");

            if (c.methods.isEmpty())
                toReturn.append("└─ No methods\n");
            else{
                toReturn.append("└─ Methods:\n");

                for (ConcreteMethod m : c.methods.values()){
                    if (c.methods.values().toArray()[c.methods.size()-1] == m){
                        toReturn.append("    └─ ").append(m.name.getLexeme()).append(" : ").append(m.type.getLexeme()).append("\n");
                        if (!m.isStatic.getLexeme().equals("-"))
                            toReturn.append("        ├─ Static: ").append(m.isStatic.getLexeme()).append("\n");
                        if (m.parameters.isEmpty())
                            toReturn.append("        └─ No parameters\n");
                        else
                            toReturn.append("        └─ Parameters:\n");
                        for (ConcreteAttribute p : m.parameters.values()){
                            if (m.parameters.values().toArray()[m.parameters.size()-1] == p)
                                toReturn.append("            └─ ").append(p.name.getLexeme()).append(" : ").append(p.type.getLexeme()).append("\n");
                            else
                                toReturn.append("            ├─ ").append(p.name.getLexeme()).append(" : ").append(p.type.getLexeme()).append("\n");
                        }
                    }else{
                        toReturn.append("    ├─ ").append(m.name.getLexeme()).append(" : ").append(m.type.getLexeme()).append("\n");
                        if (!m.isStatic.getLexeme().equals("-"))
                            toReturn.append("    │   ├─ Static: ").append(m.isStatic.getLexeme()).append("\n");                        toReturn.append("    │   └─ Parameters:\n");
                        for (ConcreteAttribute p : m.parameters.values()){
                            if (m.parameters.values().toArray()[m.parameters.size()-1] == p)
                                toReturn.append("    │       └─ ").append(p.name.getLexeme()).append(" : ").append(p.type.getLexeme()).append("\n");
                            else
                                toReturn.append("    │       ├─ ").append(p.name.getLexeme()).append(" : ").append(p.type.getLexeme()).append("\n");
                        }
                    }
                }
            }
            toReturn.append("--------------------------------\n");
        }

        for (ConcreteClass c : classes.values()){
            toReturn.append("Class: ").append(c.name.getLexeme()).append("\n");
            if (!c.extendsName.getLexeme().equals("-"))
                toReturn.append("├─ Extends: ").append(c.extendsName.getLexeme()).append("\n");
            if (!c.implementsName.getLexeme().equals("-"))
                toReturn.append("├─ Implements: ").append(c.implementsName.getLexeme()).append("\n");
            toReturn.append("├─ Constructor:\n");
            toReturn.append("│   └─ ").append(c.constructor.name.getLexeme()).append(" : ").append(c.constructor.type.getLexeme()).append("\n");

            if (c.constructor.parameters.isEmpty())
                toReturn.append("│       └─ No parameters\n");
            else{
                toReturn.append("│       └─ Parameters:\n");
                for (ConcreteAttribute p : c.constructor.parameters.values()){
                    if (c.constructor.parameters.values().toArray()[c.constructor.parameters.size()-1] == p)
                        toReturn.append("│           └─ ").append(p.name.getLexeme()).append(" : ").append(p.type.getLexeme()).append("\n");
                    else
                        toReturn.append("│           ├─ ").append(p.name.getLexeme()).append(" : ").append(p.type.getLexeme()).append("\n");
                }
            }

            if (c.hiddenAttributes.isEmpty())
                toReturn.append("├─ No hidden attributes\n");
            else
                toReturn.append("├─ Hidden attributes:\n");

            for (ConcreteAttribute a : c.hiddenAttributes.values()){
                if (c.hiddenAttributes.values().toArray()[c.hiddenAttributes.size()-1] == a)
                    toReturn.append("│   └─ ").append(a.name.getLexeme()).append(" : ").append(a.type.getLexeme()).append("\n");
                else
                    toReturn.append("│   ├─ ").append(a.name.getLexeme()).append(" : ").append(a.type.getLexeme()).append("\n");
            }

            if (c.attributes.isEmpty())
                toReturn.append("├─ No attributes\n");
            else
                toReturn.append("├─ Attributes:\n");

            for (ConcreteAttribute a : c.attributes.values()){
                if (c.attributes.values().toArray()[c.attributes.size()-1] == a)
                    toReturn.append("│   └─ ").append(a.name.getLexeme()).append(" : ").append(a.type.getLexeme()).append("\n");
                else
                    toReturn.append("│   ├─ ").append(a.name.getLexeme()).append(" : ").append(a.type.getLexeme()).append("\n");
            }

            if (c.methods.isEmpty())
                toReturn.append("└─ No methods\n");
            else{
                toReturn.append("└─ Methods:\n");

                for (ConcreteMethod m : c.methods.values()){
                    if (c.methods.values().toArray()[c.methods.size()-1] == m){
                        toReturn.append("    └─ ").append(m.name.getLexeme()).append(" : ").append(m.type.getLexeme()).append("\n");
                        if (!m.isStatic.getLexeme().equals("-"))
                            toReturn.append("        ├─ Static: ").append(m.isStatic.getLexeme()).append("\n");
                        if (m.parameters.isEmpty())
                            toReturn.append("        ├─ No parameters\n");
                        else
                            toReturn.append("        ├─ Parameters:\n");
                        for (ConcreteAttribute p : m.parameters.values()){
                            if (m.parameters.values().toArray()[m.parameters.size()-1] == p)
                                toReturn.append("        │   └─ ").append(p.name.getLexeme()).append(" : ").append(p.type.getLexeme()).append("\n");
                            else
                                toReturn.append("        │   ├─ ").append(p.name.getLexeme()).append(" : ").append(p.type.getLexeme()).append("\n");
                        }
                    }else{
                        toReturn.append("    ├─ ").append(m.name.getLexeme()).append(" : ").append(m.type.getLexeme()).append("\n");
                        if (!m.isStatic.getLexeme().equals("-"))
                            toReturn.append("    │   ├─ Static: ").append(m.isStatic.getLexeme()).append("\n");
                        if (m.parameters.isEmpty())
                            toReturn.append("    │   ├─ No parameters\n");
                        else
                            toReturn.append("    │   ├─ Parameters:\n");
                        for (ConcreteAttribute p : m.parameters.values()){
                            if (m.parameters.values().toArray()[m.parameters.size()-1] == p)
                                toReturn.append("    │   │   └─ ").append(p.name.getLexeme()).append(" : ").append(p.type.getLexeme()).append("\n");
                            else
                                toReturn.append("    │   │   ├─ ").append(p.name.getLexeme()).append(" : ").append(p.type.getLexeme()).append("\n");
                        }
                    }
                }
            }
            toReturn.append("--------------------------------\n");
        }

        return toReturn.toString();
    }

    public void checkNamesAndTypes() throws SemanticException {
        for (ConcreteClass c : classes.values()){
            if (!c.name.getLexeme().equals("Object") && !c.name.getLexeme().equals("System") && !c.name.getLexeme().equals("String"))
                c.checkNamesAndTypes();
        }
    }

    public Collection<? extends Exception> getErrors() {
        return errors;
    }

    public void generate() throws CompiException {
        codeGenerator = new CodeGenerator("generation");

        generateMainMethodCall(codeGenerator);
        generateMalloc(codeGenerator);
        generateEmptyStringTag(codeGenerator);

        for (ConcreteClass c : classes.values()){
            c.generate(codeGenerator);
        }
    }

    private void generateMainMethodCall(CodeGenerator codeGenerator) throws CompiException {

        String mainMethodTag = TagHandler.getMethodTag(mainMethod);

        codeGenerator.gen(".CODE");

        String c1 = " # We put main method's tag at the top of the stack";
        codeGenerator.gen("PUSH " + mainMethodTag + c1);

        String c2 = " # We jump to main method";
        codeGenerator.gen("CALL"+c2);

        String c3 = " # End of program.";
        codeGenerator.gen("HALT"+c3);
    }

    private void generateMalloc(CodeGenerator codeGenerator) throws CompiException {

        String tag = TagHandler.getMallocTag();

        codeGenerator.gen(tag + ": LOADFP");
        codeGenerator.gen("LOADSP");
        codeGenerator.gen("STOREFP");
        codeGenerator.gen("LOADHL");
        codeGenerator.gen("DUP");
        codeGenerator.gen("PUSH 1");
        codeGenerator.gen("ADD");
        codeGenerator.gen("STORE 4");
        codeGenerator.gen("LOAD 3");
        codeGenerator.gen("ADD");
        codeGenerator.gen("STOREHL");
        codeGenerator.gen("STOREFP");
        codeGenerator.gen("RET 1");
    }

    private void generateEmptyStringTag(CodeGenerator codeGenerator) throws CompiException {
        codeGenerator.gen(".DATA");
        codeGenerator.gen("str_empty" + ": DW 0");
    }

}