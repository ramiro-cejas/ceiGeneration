///meow&woof&moo&exitosamente
interface Animal {
    String sound();
}

class Cat implements Animal {
    void playWithYarn() {}

    String sound() {
        return "meow";
    }
}


class Dog implements Animal {
    void nap() {}

    String sound() {
        return "woof";
    }

    void runAfterMailman() {}
}


class Cow implements Animal {
    void waveTail() {}
    void eatGrass() {}
    String sound() {
        return "moo";
    }


}

class Init {
    static Animal animal;

    static void main() {
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