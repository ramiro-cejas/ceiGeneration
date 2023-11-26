package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;

public class Variable {

    public static int classID = 0;
    protected int instanceID;
    public static final int DEFAULT_OFFSET = Integer.MIN_VALUE;
    public static final int PARAMETER_MIN_OFFSET = 3;
    public static final int METHOD_MIN_OFFSET = 0;
    public static final int ATTRIBUTE_MIN_OFFSET = 1;
    public static final int LOCAL_VAR_MIN_OFFSET = 0;
    protected ConcreteClass memberOf;
    protected int offset;
    protected Token token;

    public Variable(Token t, ConcreteClass memberOf) {
        this.token = t;
        this.memberOf = memberOf;
        instanceID = classID;
        classID++;
        offset = DEFAULT_OFFSET;
    }

    public boolean isStatic() {
        return false;
    }

    public ConcreteClass getMemberOf() {
        return memberOf;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}