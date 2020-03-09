package Recursie;

public class Som {
    public int sum_recursion(int n) {
        if (n == 1) {
            return 1;
        }
        return sum_recursion(n-1) + n;
    }

    public int sum(int n) {
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result += i;
        }
        return result;
    }
}
