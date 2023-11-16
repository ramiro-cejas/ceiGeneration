///[Error:=|9]
class Main {
    static void main() {}

    void m() {
        var b = 1;
        var i = 3.14;

        b = i; //int es coercionado a float, pero float NO es coercionado a int.
    }
}