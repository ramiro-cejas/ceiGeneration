///a&1&b&3&A1&A2&B1&B2&exitosamente
interface PInterface {
    char getChar();
}

interface Interface extends PInterface {
    int cat(int i);
}

class ImplA implements Interface {

    public void mA1() {
        System.printC('A');
        System.printIln(1);
    }
    public char getChar() {
        return 'a';
    }

    public void mA2() {
        System.printC('A');
        System.printIln(2);
    }

    public int cat(int i) {
        return i;
    }
}

class ImplB implements Interface {
    public void mB1() {
        System.printC('B');
        System.printIln(1);
    }
    public void mB2() {
        System.printC('B');
        System.printIln(2);
    }
    public int cat(int i) {
        return i*3;
    }

    public char getChar() {
        return 'b';
    }
}

class Client {
    public void use(Interface i) {
        var p1 = i.getChar();
        var p2 = i.cat(1);
        System.printCln(p1);
        System.printIln(p2);
    }
}

class Main {
    public static void main() {
        var a = new ImplA();
        var b = new ImplB();
        var client = new Client();

        client.use(a);              //a \n 1
        client.use(b);              //b \n 3

        System.println();

        a.mA1();                    //A1
        a.mA2();                    //A2

        System.println();

        b.mB1();                    //B1
        b.mB2();                    //B2
    }
}

