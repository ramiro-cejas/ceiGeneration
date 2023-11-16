///[SinErrores]
class Main {
    static void main() {}

    static A attrA;
    static B attrB;

    int dynamic;

    static void m2() {
        (new A()).x = 1;
    }
}

class A {
    int x;
    int m() {
        return 3;
    }
}

class B {
    A getA() {
        return new A();
    }
}

class C {
    static int m() {
        return 3;
    }
}