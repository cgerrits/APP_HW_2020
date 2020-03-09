package Generics;

import java.util.Arrays;

public class Main {
    public static <T extends Comparable> T min(T[] array) {
        T minValue = array[0];
        for(T element: array) {
            if(minValue.compareTo(element) > 0) {
                minValue = element;
            }
        }
        return minValue;
    }

    public static void main(String[] args) {
        Address[] addresses = new Address[4];
        addresses[0] = new Address("Berkelstraat", 34);
        addresses[1] = new Address("Berkelstraat", 32);
        addresses[2] = new Address("Reggestraat", 32);
        addresses[3] = new Address("Bloemenstraat", 32);

        System.out.println("Before sorting:");
        System.out.println(Arrays.toString(addresses));
        Arrays.sort(addresses);
        System.out.println("After sorting:");
        System.out.println(Arrays.toString(addresses));
    }
}
