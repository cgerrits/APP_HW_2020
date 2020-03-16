package Toets;

import Trees.Order;

public class Main {
    public static void main(String[] args) {
        IdentifactionKey<Animal> key = new IdentifactionKey<>(null, "Leeft het op land?");
        IdentifactionKey<Animal> fruit = new IdentifactionKey<>(null, "Eet het fruit?");
        IdentifactionKey<Animal> dolfijn = new IdentifactionKey<>(new Animal("dolfijn"), null);
        IdentifactionKey<Animal> olifant = new IdentifactionKey<>(new Animal("olifant"), null);
        IdentifactionKey<Animal> aap = new IdentifactionKey<>(new Animal("aap"), null);
        key.addLeft(dolfijn);
        key.addRight(fruit);
        fruit.addLeft(olifant);
        fruit.addRight(aap);
        System.out.println(key.getLeft().getSpecies().getName());
        key.extend("Heeft het kieuwen", new Animal("tonijn"));
        System.out.println(key.getLeft().getQuestion());
    }
}
