package SecondSemantic.Semantic;

public class TypeClass implements Type{
    public String name;
    public TypeClass(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean isSubtype(Type other) {
        return false;
    }
}
