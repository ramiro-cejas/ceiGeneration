package SecondSemantic.Semantic;

public class TypePrimitive implements Type{
    public String name;

    public TypePrimitive(String name) {
        this.name = name;
    }

    @Override
    public boolean isSubtype(Type other) {
        if (other instanceof TypePrimitive)
            return name.equals(((TypePrimitive) other).name);
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
