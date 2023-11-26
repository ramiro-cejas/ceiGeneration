package SecondSemantic.Generation;

import SecondSemantic.Semantic.ConcreteAttribute;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.Nodes.Node;
import SecondSemantic.Semantic.Nodes.NodeBlock;

public class TagHandler {
    static int lastTagID = 0;
    static int sentenceID = 0;

    public static String getMethodTag(ConcreteMethod m) {
        String c_method = m.name.getLexeme();
        String c_class = m.methodBlock.currentClass.name.getLexeme();
        return ("Method_" + c_method + "@" + c_class);
    }

    public static String getConstructorTag(ConcreteMethod c) {
        System.out.println(c.name.getLexeme());
        String c_class = c.methodBlock.currentClass.name.getLexeme();
        return ("Constructor@" + c_class);
    }

    public static String getVTableTag(ConcreteClass c) {
        return "VT@" + c.name.getLexeme();
    }

    public static String getAttributeTag(ConcreteAttribute a, ConcreteClass c) {
        String c_attr = a.getName().getLexeme();
        String c_class = c.name.getLexeme();
        return (c_attr + "@" + c_class);
    }

    public static String getSentenceTag(Node s, NodeBlock b) {
        String lexeme = s.getToken().getLexeme();
        String c_method = b.currentMethod.name.getLexeme();
        String c_class = b.currentClass.name.getLexeme();
        String tag = lexeme + "@" + c_method + "@" + c_class + lastTagID;

        lastTagID++;

        return tag;
    }

    public static String getMallocTag() {
        return "malloc";
    }

    public static int getSentenceID() {
        int toReturn = sentenceID;
        sentenceID++;
        return toReturn;
    }
}
