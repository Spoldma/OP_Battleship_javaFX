package com.example.oop_projekt_javafx;

import javafx.scene.image.Image;

public class KuvaVäli extends Väli {
    private String[][] väli;

    public KuvaVäli(String[][] Väli) {
        this.väli = Väli;
    }

    public KuvaVäli(boolean[][] väli){
        String[][] uus = new String[väli.length][väli.length];
        for (int i = 0; i < väli.length; i++) {
            for (int j = 0; j < väli.length; j++) {
                uus[i][j] = ".";
            }
        }
        this.väli = uus;
    }

    public String[][] getVäli() {
        return väli;
    }

    public void setVäli(String[][] v) {
        this.väli = v;
    }

    public void uuendaVäli(int i, int j, boolean pihtas) {
        for (int k = 0; k < getVäli().length; k++) {
            for (int l = 0; l < getVäli().length; l++) {
                if (pihtas)
                    getVäli()[i][j] = "X";
                else
                    getVäli()[i][j] = "O";
            }
        }
    }

    public int loeTäisTabamusi() {
        int tabamused = 0;
        int n = getVäli().length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (getVäli()[i][j].equals("X"))
                    tabamused++;
            }
        }
        return tabamused;
    }

}
