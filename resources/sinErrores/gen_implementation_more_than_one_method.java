///0: meow&yarn&1: woof&bone toy!&2: moo&c&exitosamente
interface Animal {
    String sound();
    int id();
    void play();
}

class Cat implements Animal {
    int id() {
        return 0;
    }
    void playWithYarn() {
        System.printSln("yarn");
    }

    String sound() {
        return "meow";
    }

    void play() {
        this.playWithYarn();
    }
}


class Dog implements Animal {
    void play() {
        System.printSln("bone toy!");
    }
    void nap() {}

    int id() {
        return 1;
    }

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

    void play() {
        System.printCln('c');
    }

    int id() {
        return 2;
    }
}

class Init {
    static Animal animal;

    static void main() {
        var i = 0;
        var id = 0;
        while(i < 3) {

            if(i == 0) {
                animal = new Cat();
            } else if(i == 1) {
                animal = new Dog();
            } else {
                animal = new Cow();
            }

            id = animal.id();
            System.printI(id);
            System.printS(": ");
            System.printSln(animal.sound());
            animal.play();

            i = i + 1;
        }
    }
}