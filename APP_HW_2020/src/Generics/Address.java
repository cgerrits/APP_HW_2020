package Generics;

public class Address implements Comparable<Address>{
    private String street;
    private int houseNumber;

    public Address(String street, int houseNumber) {
        this.street = street;
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        return street + " " + houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    @Override
    public int compareTo(Address o) {
        if (street.compareTo(o.getStreet()) == 0) {
            return houseNumber - o.getHouseNumber();
        }
        return street.compareTo(o.getStreet());
    }
}
