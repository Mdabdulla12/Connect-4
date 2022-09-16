package com.example.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Controller controller;
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("game.fxml"));
       GridPane rootGridPane=fxmlLoader.load();
        controller=fxmlLoader.getController();
        controller.createPlayground();



        MenuBar menuBar=createMenu();
        menuBar.prefWidthProperty().bind(stage.widthProperty());

        Pane menuPane=(Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);
        Scene scene=new Scene(rootGridPane);

        stage.setTitle("Connect 4");
        stage.setScene(scene);
        stage.show();
    }
    private  MenuBar createMenu(){
        //Menu
        Menu fileMenu=new Menu("File");
        MenuItem newGame=new MenuItem("New Game");
        newGame.setOnAction(actionEvent ->controller.resetGame());
        MenuItem resetGame=new MenuItem("Reset Game");
        resetGame.setOnAction(actionEvent -> controller.resetGame());
        SeparatorMenuItem separatorMenuItem=new SeparatorMenuItem();
        MenuItem exitGame=new MenuItem("Exit Game");
        exitGame.setOnAction(actionEvent -> exitGame());

        fileMenu.getItems().addAll(newGame,resetGame,exitGame);

        Menu helpMenu=new Menu("Help");
        MenuItem aboutGame=new MenuItem("About Connect 4");
        aboutGame.setOnAction(actionEvent -> aboutConnect4());
        SeparatorMenuItem separatorMenuItem1=new SeparatorMenuItem();
        MenuItem aboutMe=new MenuItem("About developer");
        aboutMe.setOnAction(actionEvent -> aboutMe());

        helpMenu.getItems().addAll(aboutGame,aboutMe);


        MenuBar menuBar=new MenuBar();
        menuBar.getMenus().addAll(fileMenu,helpMenu);
        return menuBar;



    }

    private void aboutMe() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the developer");
        alert.setHeaderText("Md.Abdullah.Gaur");
        alert.setContentText("I love to play around with code and create games\n"+
                             "Connect 4 is one them.In free time"+
                              "\nI like to spend time near and dears");
        alert.show();
    }

    private void aboutConnect4() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect 4");
        alert.setHeaderText("HOW TO PLAY?");
        alert.setContentText("Connect Four is a two-player connection game in which the\n" +
                    "players first choose a color and then take turns dropping colored discs"+
                   "\nfrom the top into a seven-column, six-row vertically suspended grid." +
                   "\nThe pieces fall straight down, occupying the next available space " +
                   "within the column." +
                   "\nThe objective of the game is to be the first to form a horizontal," +
                   " vertical,or\n"+
                  "diagonal line of four of one's own discs. Connect Four is a solved game" +
                  "\nThe first player can always win by playing the right moves.");
        alert.show();
    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }




}