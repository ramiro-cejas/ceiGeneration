///1&1&1&1&2&2&true&exitosamente

class A {
    int x;
}
class Main {
    static int a;
    static int b;
    static int c;
    static int d;
    static void main() {
        a = b = c = d = 1;
        System.printIln(a);
        System.printIln(b);
        System.printIln(c);
        System.printIln(d);

        method();
        colateral();
    }

    static void method() {
        var a1 = new A();
        var a2 = new A();

        a1.x = a2.x = 2;

        debugPrint(a1.x);
        debugPrint(a2.x);
    }

    static void colateral() {
        var c = 3;
        var x = (c = 5) > 3;
        System.printBln(x);
    }
}