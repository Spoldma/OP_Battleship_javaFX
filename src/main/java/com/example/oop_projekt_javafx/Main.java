package com.example.oop_projekt_javafx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.setProperty("file.encoding", "UTF-8");
        //ns Väli isend
        Väli mäng = new Väli();

        //kui suur väli + arvuti genereeritud väljad
        int väljaSuurus = küsiSuurus();
        int paatideArv = küsiPaat(väljaSuurus);

        MänguVäli pcBoolean = new MänguVäli(teeVäliBoolean(väljaSuurus, paatideArv));
        KuvaVäli pcString = new KuvaVäli(teeVäliString(väljaSuurus));

        //playeri väli
        MänguVäli playerBoolean = new MänguVäli(teeVäliBoolean(väljaSuurus, 0));
        paigutaLaevad(playerBoolean, paatideArv);
        KuvaVäli playerString = new KuvaVäli(teeVäliString(playerBoolean.getVäli()));

        //Mäng algab
        System.out.println("Mäng algab!");
        System.out.println("Vasakul on sinu laevad | Paremal on vaenlase laevad");
        mäng.prindiVäli(playerString.getVäli(), pcString.getVäli());

        while (pcString.loeTäisTabamusi() != paatideArv && playerString.loeTäisTabamusi() != paatideArv) {
            küsiLasku(pcBoolean, pcString);
            mäng.prindiVäli(playerString.getVäli(), pcString.getVäli());
            if (pcString.loeTäisTabamusi() != paatideArv) {
                System.out.println("Vaenlase kord!");
                punkt(1000);
                arvutiLasud(playerBoolean, playerString);
                mäng.prindiVäli(playerString.getVäli(), pcString.getVäli());
            }
            else
                continue;
        }
        if (pcString.loeTäisTabamusi() > playerString.loeTäisTabamusi()) {
            System.out.println("Mäng läbi, võitsid sina!");
        }
        else
            System.out.println("Mäng läbi! Võitis vaenalne");
    }

    //genereerib suvalise n*n boolean maatriksi, milles etteantud arv ühtesi
    public static boolean[][] teeVäliBoolean(int n, int laevu) {
        boolean[][] väli = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                väli[i][j] = false;
            }
        }
        for (int i = 1; i <= laevu; i++) {
            while (true) {
                int random1 = (int) (Math.random() * (n));
                int random2 = (int) (Math.random() * (n));
                if (!väli[random1][random2]) {
                    väli[random1][random2] = true;
                    break;
                }

            }
        }
        return väli;
    }

    //teeb uue maatriksi vastavalt etteantud boolean maatriksile, kus true="+" ja false="-"
    public static String[][] teeVäliString(boolean[][] x) {
        int n = x.length;
        String[][] väli = new String[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (x[i][j])
                    väli[i][j] = "+";
                else
                    väli[i][j] = "-";
            }

        }
        return väli;
    }

    //loob n*n maatriksi mille kõikidel kohtadel "."
    public static String[][] teeVäliString(int n) {
        String[][] väli = new String[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                väli[i][j] = ".";
            }
        }
        return väli;
    }

    //küsib mängijalt välja suuruse ja paatide arvu
    public static int küsiSuurus() throws IOException {
        int suurus = 0;
        while (true) {
            try {
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Sisesta välja suurus (2 - 10): ");
                String sõne = reader1.readLine();

                suurus = Integer.parseInt(sõne);
                if (suurus < 2 || suurus > 10){
                    System.out.println("Sisestatud andmed ei sobi! Proovi uuesti!");
                    continue;
                }
                break;

            }catch(Exception e) {
                System.out.println("Sisestatud andmed ei sobi! Proovi uuesti!");
            }
        }
        return suurus;
    }

    public static int küsiPaat(int suurus) throws IOException {
        int paate = 0;
        while (true) {
            try {
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(System.in));
                int maxPaadid = (int) (Math.pow(suurus, 2))-1;
                System.out.print("Sisesta paatide arv (1 - " + maxPaadid + "): ");
                String paatideArv = reader2.readLine();
                paate = Integer.parseInt(paatideArv);
                if (paate < 1 || paate > maxPaadid){
                    System.out.println("Sisestatud andmed ei sobi! Proovi uuesti!");
                    continue;
                }
                System.out.println();
                break;
            }catch(Exception e) {
                System.out.println("Sisestatud andmed ei sobi! Proovi uuesti!");
            }
        }
        return paate;
    }

    public static void küsiLasku(MänguVäli pcBoolean, KuvaVäli pcString) throws IOException {
        int väljaSuurus = pcBoolean.getVäli().length;
        int rida = 0;
        int veerg = 0;

        while (true) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Sisesta laskmiseks koordinaadid (rida,veerg): ");
                String[] koordinaadid = reader.readLine().split(",");
                rida = Integer.parseInt(koordinaadid[0]);
                veerg = Integer.parseInt(koordinaadid[1]);
                if (!pcString.getVäli()[rida][veerg].equals("X") && !pcString.getVäli()[rida][veerg].equals("O")) {
                    punkt(300);
                    if (rida < väljaSuurus && veerg < väljaSuurus && pcBoolean.pihtaMööda(rida, veerg)) {
                        pcBoolean.eemaldaPaat(rida, veerg);
                        pcString.uuendaVäli(rida, veerg, true);
                        System.out.println("Pihtas!");
                        break;
                    } else if (rida < väljaSuurus && veerg < väljaSuurus) {
                        pcBoolean.lisaPaat(rida, veerg);
                        pcString.uuendaVäli(rida, veerg, false);
                        System.out.println("Lasid mööda!");
                        break;
                    }
                } else {
                    System.out.println("Juba lasid sinna, proovi uuesti!");
                }
            } catch (Exception e) {
                System.out.println("Sisestatud koordinaadid pole väljal! Proovi uuesti!");

            }
        }
    }

    public static void arvutiLasud(MänguVäli playerBoolean, KuvaVäli playerString) {
        int väljaSuurus = playerBoolean.getVäli().length;

        while (true) {
            int rida = (int) (Math.random() * väljaSuurus);
            int veerg = (int) (Math.random() * väljaSuurus);
            if (!playerString.getVäli()[rida][veerg].equals("X") && !playerString.getVäli()[rida][veerg].equals("O")) {
                if (playerBoolean.pihtaMööda(rida, veerg)) {
                    playerBoolean.eemaldaPaat(rida, veerg);
                    playerString.uuendaVäli(rida, veerg, true);
                    System.out.println("Vaenlane lasi pihta!");
                    break;
                } else {
                    playerBoolean.lisaPaat(rida, veerg);
                    playerString.uuendaVäli(rida, veerg, false);
                    System.out.println("Vaenlane lasi mööda!");
                    break;
                }
            }
        }
    }

    //laseb mängijal paigutada oma laevad
    public static void paigutaLaevad(MänguVäli playerBoolean, int paate) throws IOException {
        System.out.println("Väljale paigutavate paatide arv: " + paate);
        int väljaSuurus = playerBoolean.getVäli().length;
        int paateAlles = paate;
        while (paateAlles > 0) {
            try {
                System.out.println("Sinu praegune väli on selline: ");
                playerBoolean.prindiVäli(teeVäliString(playerBoolean.getVäli()));
                System.out.println("Paate veel sisestada: " + paateAlles);
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Sisesta paadi koordinaadid (kujul rida,veerg): ");
                String[] koordinaadid = reader.readLine().split(",");
                if (Integer.parseInt(koordinaadid[0]) < väljaSuurus && Integer.parseInt(koordinaadid[1]) < väljaSuurus) {
                    if (!playerBoolean.getVäli()[Integer.parseInt(koordinaadid[0])][Integer.parseInt(koordinaadid[1])]) {
                        playerBoolean.lisaPaat(Integer.parseInt(koordinaadid[0]), Integer.parseInt(koordinaadid[1]));
                        System.out.println();
                        paateAlles--;
                    } else {
                        System.out.println("Seal on juba paat, proovi uuesti!");
                    }
                } else {
                    System.out.println("Sisestatud koordinaadid pole väljal! Proovi uuesti!");
                }
            }catch (Exception e) {
                System.out.println("Sisestatud koordinaadid pole väljal! Proovi uuesti!");
            }
        }
    }

    public static void  punkt(int aeg) throws InterruptedException {
        Thread.sleep(aeg);
        System.out.print(". ");
        Thread.sleep(aeg);
        System.out.print(". ");
        Thread.sleep(aeg);
        System.out.println(". ");
        Thread.sleep(aeg);
    }
}
