package main.java.util;

public class Util {
    public String deleteEnd(String s) {
        if (s.length() > 0) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
