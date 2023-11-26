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
        var a = new A();
        a.methodToCheck();
        //A.methodToPrintAndTest(4,5);
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
}