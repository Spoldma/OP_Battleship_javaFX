package com.example.oop_projekt_javafx;
public class MänguVäli extends Väli {
    private final boolean[][] väli;

    public MänguVäli(boolean[][] väli) {
        this.väli = väli;
    }

    public boolean pihtaMööda(int i, int j) {
        return this.väli[i][j];
    }

    public void eemaldaPaat(int i, int j) {
        this.väli[i][j] = false;
    }

    public void lisaPaat(int i, int j) {
        this.väli[i][j] = true;
    }

    public boolean[][] getVäli() {
        return väli;
    }
}
