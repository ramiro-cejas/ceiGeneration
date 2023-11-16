class A {
    static void main() {
        var a = 1;
        var b = false;
        var c = "hola";
        var d = 1.0;
        var e = 'a';
        var f = new B(3);
        //a = f.m2("2").m3();

    }
}

class B{

    boolean b;
    public B(int a){

    }

    static void m1(){

    }

    B m2(String s){
        return new B(4);
    }

    int m3(){
        return 2;
    }
}