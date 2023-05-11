package com.example.oop_projekt_javafx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GraafilineLiides extends Application {

    private Stage primaryStage;
    private int väljaSuurus;
    private int paatideArv;
    private int abiPaatideArv;
    private int playerPaateJärel;
    private int pcPaateJärel;
    private final int MAX_ROWS = 10;
    private final int MAX_COLS = 10;
    private GridPane playerGridPane;
    private GridPane pcGridPane;
    private MänguVäli playerBooleanGrid;
    private MänguVäli pcBooleanGrid;
    private KuvaVäli playerStringGrid;
    private KuvaVäli pcStringGrid;
    private boolean playerRuudustik = false;
    private boolean pcRuudustik = false;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("OP_Battleship");
        showKüsiVäärtused();

    }

    private void showKüsiVäärtused() {
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 400, 400);

        vBox.setStyle("-fx-background-color: #5A5A5A;");
        vBox.setAlignment(Pos.CENTER);

        Label labelVäli = new Label("Sisesta välja suurus (2 - 8):");
        TextField field1 = new TextField();
        labelVäli.setTextFill(Color.WHITE);

        vBox.getChildren().addAll(labelVäli, field1);

        Button button1 = new Button("Next");

        button1.setOnAction(e -> {
            if (field1.getText().matches("\\d+") && 2<=Integer.parseInt(field1.getText()) && Integer.parseInt(field1.getText())<=8) {
                väljaSuurus = Integer.parseInt(field1.getText());

                int maxPaadid = (int) (Math.pow(väljaSuurus, 2)) - 1;
                Label labelPaadid = new Label("Sisesta paatide arv (1 - " + maxPaadid + "):");
                TextField field2 = new TextField();
                labelPaadid.setTextFill(Color.WHITE);
                vBox.getChildren().addAll(labelPaadid, field2);
                Button button2 = new Button("Start game!");
                button2.setOnAction(r -> {
                    if (field2.getText().matches("\\d+") && 1 <= Integer.parseInt(field2.getText()) && Integer.parseInt(field2.getText()) <= maxPaadid) {
                        abiPaatideArv = playerPaateJärel = pcPaateJärel = paatideArv = Integer.parseInt(field2.getText());
                        try {
                            showPaatidePaigutus();
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                vBox.getChildren().add(button2);
            }
        });
        vBox.getChildren().add(button1);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showPaatidePaigutus() throws FileNotFoundException {
        // Create the next scene
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 400, 400);


        vBox.setStyle("-fx-background-color: #5A5A5A;");
        vBox.setAlignment(Pos.CENTER);

        Label label = new Label("Nüüd pead paigutama paadid oma väljale!" + "\n" +
                "Paatide paigutamiseks kliki väljal (paate veel paigutada: " + abiPaatideArv + "): ");
        label.setTextFill(Color.WHITE);
        if (!playerRuudustik) {
            playerGridPane = ruudustik(väljaSuurus, väljaSuurus);
        }
        vBox.getChildren().addAll(label, playerGridPane);

        Button button2 = new Button("OK");
        button2.setOnAction(e -> {
            if (abiPaatideArv > 0) {
                try {
                    showPaatidePaigutus();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                showMäng();
            }
        });
        vBox.getChildren().add(button2);

        primaryStage.setScene(scene);
        if (väljaSuurus>6) {
            primaryStage.setWidth(väljaSuurus*50+200);
            primaryStage.setHeight(väljaSuurus*50+200);
        }
        primaryStage.show();
    }

    private void showMäng() {
        Label label = new Label("Mäng algas!" + "\n"
                + "Üleval on sinu laevad(" + playerPaateJärel + " veel alles) | All on vaenlase laevad ("
                + pcPaateJärel + " veel alles)"); //ei tööta
        if (!pcRuudustik) {
            pcBooleanGrid = new MänguVäli(teeVäliBoolean(väljaSuurus, paatideArv));
            pcStringGrid = new KuvaVäli(pcBooleanGrid.getVäli());
            playerStringGrid = new KuvaVäli(pcBooleanGrid.getVäli());
            try {
                pcGridPane = arvutiRuudustik(väljaSuurus, väljaSuurus);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            pcRuudustik = true;
        }
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 400, väljaSuurus*100+100);
        vBox.setStyle("-fx-background-color: #5A5A5A;");
        vBox.setAlignment(Pos.CENTER);


        label.setTextFill(Color.WHITE);
        vBox.setAlignment(Pos.CENTER);
        pcGridPane.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label, playerGridPane, pcGridPane);

        vBox.setSpacing(10);

        try {
            lasePlayer();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showEndPC() {
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: #5A5A5A;");
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 400, 400);
        Label label = new Label("Mäng läbi," + "\n" + "võitis Arvuti!");
        label.setTextFill(Color.WHITE);
        Button button = new Button("Mängi uuesti?");
        button.setOnAction(r -> {
            pcRuudustik = false;
            playerRuudustik = false;
            showKüsiVäärtused();
        });
        vBox.getChildren().addAll(label, button);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void showEndPlayer() {
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: #5A5A5A;");
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 400, 400);
        Label label = new Label("Mäng läbi," + "\n" + "võitsid Sina!");
        label.setTextFill(Color.WHITE);
        Button button = new Button("Mängi uuesti?");
        button.setOnAction(r -> {
            pcRuudustik = false;
            playerRuudustik = false;
            showKüsiVäärtused();
        });
        vBox.getChildren().addAll(label, button);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane ruudustik(int numRows, int numCols) throws FileNotFoundException {
        Image algne = new Image(new FileInputStream("mangija_tava.PNG"));
        Image paat = new Image(new FileInputStream("mangija_paat.PNG"));

        GridPane gridPane = new GridPane();

        if (!playerRuudustik) {
            playerBooleanGrid = new MänguVäli(new boolean[väljaSuurus][väljaSuurus]);
            playerRuudustik = true;
        }

        gridPane.setMaxSize(50 * numCols, 50 * numRows);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                ImageView imageView = new ImageView(algne);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (abiPaatideArv > 0) {
                            if (!playerBooleanGrid.getVäli()[GridPane.getRowIndex(imageView)][GridPane.getColumnIndex(imageView)]) {
                                abiPaatideArv--;
                                playerBooleanGrid.lisaPaat(GridPane.getRowIndex(imageView), GridPane.getColumnIndex(imageView));
                            }

                            System.out.println("You clicked on the square at row " + GridPane.getRowIndex(imageView) +
                                    " and column " + GridPane.getColumnIndex(imageView));
                            ImageView clickedImageView = (ImageView) event.getSource();

                            clickedImageView.setImage(paat);
                            try {
                                showPaatidePaigutus();
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
                gridPane.add(imageView, col, row);
            }
        }
        return gridPane;
    }

    public void lasePC() throws FileNotFoundException {
        Image möödas = new Image(new FileInputStream("mangija_mooda.PNG"));
        Image pihtas = new Image(new FileInputStream("mangija_pihtas.PNG"));
        ImageView imageViewM = new ImageView(möödas);
        imageViewM.setFitWidth(50);
        imageViewM.setFitHeight(50);
        ImageView imageViewP = new ImageView(pihtas);
        imageViewP.setFitWidth(50);
        imageViewP.setFitHeight(50);

        while (true) {
            int rida = (int) (Math.random() * väljaSuurus);
            int veerg = (int) (Math.random() * väljaSuurus);
            if (playerStringGrid.getVäli()[rida][veerg].equals(".")) {
                if (playerBooleanGrid.pihtaMööda(rida, veerg)) {
                    playerStringGrid.uuendaVäli(rida, veerg, true);
                    System.out.println("Vaenlane lasi pihta!");
                    playerGridPane.add(imageViewP, veerg, rida);
                    playerPaateJärel--;
                    showMäng();
                    break;
                } else {
                    playerStringGrid.uuendaVäli(rida, veerg, false);
                    System.out.println("Vaenlane lasi mööda!");
                    playerGridPane.add(imageViewM, veerg, rida);
                    showMäng();
                    break;
                }

            }
        }
        if (playerPaateJärel==0){
            showEndPC();
        }
    }


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

    public GridPane arvutiRuudustik(int numRows, int numCols) throws FileNotFoundException {
        Image algne = new Image(new FileInputStream("vaenlane_tava.PNG"));
        GridPane gridPane = new GridPane();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                ImageView imageView = new ImageView(algne);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                gridPane.add(imageView, col, row);
            }
        }
        return gridPane;
    }

    public void lasePlayer() throws FileNotFoundException {
        Image möödas = new Image(new FileInputStream("vaenlane_mooda.PNG"));
        Image pihtas = new Image(new FileInputStream("vaenlane_pihtas.PNG"));

        for (Node node : pcGridPane.getChildren()) {
            node.setOnMouseClicked(event -> {
                int rida = GridPane.getRowIndex(node);
                int veerg = GridPane.getColumnIndex(node);
                if (pcStringGrid.getVäli()[rida][veerg].equals(".")) {
                    if (pcBooleanGrid.pihtaMööda(rida, veerg)) {
                        pcStringGrid.uuendaVäli(rida, veerg, true);
                        System.out.println("Lasid pihta!");
                        ImageView imageViewP = new ImageView(pihtas);
                        imageViewP.setFitWidth(50);
                        imageViewP.setFitHeight(50);
                        pcGridPane.add(imageViewP, veerg, rida);
                        pcPaateJärel--;
                        showMäng();
                    } else {
                        pcStringGrid.uuendaVäli(rida, veerg, false);
                        System.out.println("Lasid mööda!");
                        ImageView imageViewM = new ImageView(möödas);
                        imageViewM.setFitWidth(50);
                        imageViewM.setFitHeight(50);
                        pcGridPane.add(imageViewM, veerg, rida);
                        showMäng();
                    }
                    if (playerPaateJärel > 0 && pcPaateJärel > 0) {
                        try {
                            punkt(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            lasePC();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else
                        if (playerPaateJärel == 0)
                            showEndPC();
                        else
                            showEndPlayer();
                }
            });
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
