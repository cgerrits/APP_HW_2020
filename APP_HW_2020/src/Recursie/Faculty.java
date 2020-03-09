package Recursie;

public class Faculty {
    public int faculty_recursion(int n) {
        if(n == 1){
            return 1;
        }
        return faculty_recursion(n-1) * n;
    }

    public int faculty(int n) {
        int result = 1;
        for (int i=2;i<=n;i++) {
            result = result * i;
        }
        return result;
    }
}
