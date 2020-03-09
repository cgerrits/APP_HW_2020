package Recursie;

public class Binary {
    public int binaryCount(int n) {
        if(n <= 1) {
            return n;
        }
        return n % 2 + binaryCount(n / 2);
    }
}
