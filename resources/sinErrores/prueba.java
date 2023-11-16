class A {
    static int a2;
}

class B {
    C m2(){
        return null;
    }
}

class C extends B {
    static A a3;
    static M m3(){
        return null;
    }
}

class M {
    int x;
    int x(){
        return 0;
    }
    void y(){}
}

class Main {
    static M a1;
    static void main(){
        C.m3().x = 4;
        a1.x = 5;
        m1(3);
        new M().y();
        (new B()).m2();
    }
    static void m1(int a1) {
        a1=4;
    }

}
