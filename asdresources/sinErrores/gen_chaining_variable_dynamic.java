///1&2&exitosamente
class A {
    public int x;

    public A(int init) {
        x = init;
    }
}

class Main {
    public static void main() {
        //we check that the constructor works
        var a = new A(1);

        //we check that read accesses work
        var p1 = a.x;

        //we check that write accesses work
        a.x = 2;

        var p2 = a.x;

        debugPrint(p1);
        debugPrint(p2);
    }
}