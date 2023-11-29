///meow&woof&moo&exitosamente
class Animal {
    String sound() {
        return " ";
    }
}

class Cat extends Animal {
    void playWithYarn() {}

    String sound() {
        return "meow";
    }
}


class Dog extends Animal {
    void nap() {}

    String sound() {
        return "woof";
    }

    void runAfterMailman() {}
}


class Cow extends Animal {
    void waveTail() {}
    void eatGrass() {}
    String sound() {
        return "moo";
    }


}

class Init {
    static void main() {
        var animal = new Animal();
        var i = 0;

        while(i < 3) {

            if(i == 0) {
                animal = new Cat();
            } else if(i == 1) {
                animal = new Dog();
            } else {
                animal = new Cow();
            }

            System.printSln(animal.sound());
            i = i + 1;
        }
    }
}