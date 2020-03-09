package Recursie;

public class StringReverse {
    public String reverse(String str) {
        if(str.isEmpty()) {
            return str;
        }
        String lastChar = str.substring(str.length()-1, str.length());
        String remainingStr = str.substring(0, str.length() -1);
        return lastChar + reverse(remainingStr);
    }
}
