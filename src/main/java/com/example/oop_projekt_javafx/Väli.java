package com.example.oop_projekt_javafx;
import java.util.ArrayList;
import java.util.Arrays;

public class Väli {

    public void prindiVäli(boolean[][] x) {
        for (boolean[] i : x) {
            System.out.println(Arrays.toString(i));
        }
    }

    public void prindiVäli(String[][] x) {
        for (String[] i : x) {
            System.out.println(Arrays.toString(i));
        }
    }

    public void prindiVäli(String[][] x, String[][] y) {
        int n = x.length;
        for (int i = 0; i < n; i++) {
            System.out.print(Arrays.toString(x[i]));
            System.out.print("  |  ");
            System.out.println(Arrays.toString(y[i]));
        }
    }
}
