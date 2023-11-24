package SecondSemantic.Semantic;

import SecondSemantic.Lexical.Token;

public class ConcreteAttribute {

    public Token isStatic;
    Token name;
    Token type;
    ConcreteClass originalClass;
    int offset;

    public ConcreteAttribute(Token name, Token type, Token isStatic) {
        this.name = name;
        this.type = type;
        this.isStatic = isStatic;
    }

    public Token getName() {
        return name;
    }

    public Token getType() {
        return type;
    }

    public Token isStatic() {
        return isStatic;
    }

    public ConcreteClass getOriginalClass() {
        return originalClass;
    }

    public void setOriginalClass(ConcreteClass originalClass) {
        this.originalClass = originalClass;
    }

    public void setOffset(int newOffset) {
        this.offset = newOffset;
    }

    public int getOffset() {
        return offset;
    }

}
