package SecondSemantic.Extras;

import java.util.HashSet;
import java.util.Set;

public class Keywords {
    private Set<String> keywords;

    public Keywords(){
        keywords = new HashSet<>();
        keywords.add("class");
        keywords.add("interface");
        keywords.add("extends");
        keywords.add("implements");
        keywords.add("public");
        keywords.add("static");
        keywords.add("void");
        keywords.add("boolean");
        keywords.add("char");
        keywords.add("int");
        keywords.add("if");
        keywords.add("else");
        keywords.add("while");
        keywords.add("return");
        keywords.add("var");
        keywords.add("this");
        keywords.add("new");
        keywords.add("null");
        keywords.add("true");
        keywords.add("false");
        keywords.add("float");
    }

    public boolean isKeyword(String toCheck){
        return keywords.contains(toCheck);
    }

}
