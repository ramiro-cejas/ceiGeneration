class A {

    int attributo1;
    char attributo2;
    boolean attributo3;
    int method1(int a, int b){
        return a + b;
    }
    char method2(int a, int b){
        return 'a';
    }
    boolean method3(int a, int b){
        return true;
    }
    void methodToCheck(){
        System.printI(1);
    }

    static void methodToPrintAndTest(int a, int b){
        System.printI(3);
    }
}

class B {
    int attributo1;
    char attributo2;
    boolean attributo3;
    static void main(){
        //var a = new A();
        var b = 'b';
        //var c = true;
        //a.methodToCheck();
        System.printC(b);
        //System.printB(c);
        //A.methodToPrintAndTest(4,5);

        //debugPrint(1);
    }
    int method1(int a, int b){
        return a + b;
    }
    char method2(int a, int b){
        return 'a';
    }
    boolean method3(int a, int b){
        return true;
    }
}

class C extends B {

    int attributo1;
    boolean method4(int a, int b){
        return true;
    }
    int method1(int a, int b){
        return a - b;
    }
}