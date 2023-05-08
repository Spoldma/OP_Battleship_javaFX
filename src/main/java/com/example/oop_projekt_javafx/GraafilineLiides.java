package com.example.oop_projekt_javafx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GraafilineLiides extends Application {

    private Stage primaryStage;
    private Stage inputStage;
    private int väljaSuurus;
    private int paatideArv;
    private final int MAX_ROWS = 10;
    private final int MAX_COLS = 10;
    private GridPane playerGridPane;

    private boolean playerRuudustik = false;

    public GraafilineLiides() throws FileNotFoundException {
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        this.primaryStage = primaryStage;

        showKüsiVäärtused();
    }
    private void showKüsiVäärtused() {
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 400, 400);

        Label labelVäli = new Label("Sisesta välja suurus (2 - 10):");
        TextField field1 = new TextField();
        vBox.getChildren().addAll(labelVäli, field1);

        Button button1 = new Button("Next");
        button1.setOnAction(e -> {
            väljaSuurus = Integer.parseInt(field1.getText());
            int maxPaadid = (int) (Math.pow(väljaSuurus, 2)) - 1;
            Label labelPaadid = new Label("Sisesta paatide arv (1 - " + maxPaadid + "):");
            TextField field2 = new TextField();
            vBox.getChildren().addAll(labelPaadid, field2);
            Button button2 = new Button("Start game!");
            button2.setOnAction(r -> {
                paatideArv = Integer.parseInt(field2.getText());
                try {
                    showPaatidePaigutus();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            });
            vBox.getChildren().add(button2);
        });
        vBox.getChildren().add(button1);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showPaatidePaigutus() throws FileNotFoundException {
        // Create the next scene
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 400, 400);

        Label label = new Label("Nüüd pead paigutama paadid oma väljale!" + "\n" +
                "Paatide paigutamiseks kliki väljal (paate veel paigutada: " + paatideArv + "): ");
        if (!playerRuudustik) {
            playerGridPane = ruudustik(väljaSuurus, väljaSuurus);
            playerRuudustik = true;
        }
        vBox.getChildren().addAll(label, playerGridPane);

        Button button2 = new Button("OK");
        button2.setOnAction(e -> {
            if (paatideArv>0) {
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
        primaryStage.show();
    }

    private void showMäng(){
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox,400,400);

        Label label = new Label("Mäng algas!" + "\n" + "Vasakul on sinu laevad | Paremal on vaenlase laevad");

        vBox.getChildren().add(label);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public GridPane ruudustik(int numRows,int numCols) throws FileNotFoundException {
        Image algne = new Image(new FileInputStream("IMG_0123.PNG"));
        Image pihtas = new Image(new FileInputStream("IMG_0124.PNG"));
        Image möödas = new Image(new FileInputStream("IMG_0125.PNG"));

        // Create the GridPane for the matrix
        GridPane gridPane = new GridPane();

        MänguVäli player = new MänguVäli(new boolean[väljaSuurus][väljaSuurus]);

        gridPane.setMaxSize(50 * numCols, 50 * numRows);

        // Add the ImageView to each cell of the matrix
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                ImageView imageView = new ImageView(algne);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (paatideArv>0) {
                            paatideArv--;
                            player.lisaPaat(GridPane.getRowIndex(imageView), GridPane.getColumnIndex(imageView));
                            System.out.println("You clicked on the square at row " + GridPane.getRowIndex(imageView) +
                                    " and column " + GridPane.getColumnIndex(imageView));
                            ImageView clickedImageView = (ImageView) event.getSource();
                            // Change the image of the clicked ImageView
                            clickedImageView.setImage(pihtas);
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


}
