package com.example.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    private static final int COLUMNS=7;
    private static final int ROWS=6;
    private static final int CIRCLE_DIAMETER=80;

    private static final String discColor1="#20303E";
    private static final String discColor2="#FFFF00";

    private static String input1;
    private static String input2;

    private static String PLAYER_ONE;
    private static String PLAYER_TWO;
    private  boolean isPlayerOneTurn=true;
    private Disc[][] insertedDiscArray=new Disc[ROWS][COLUMNS]; //structural




    @FXML
    public GridPane rootGrid;
    @FXML
    public Pane insertedDiskPane;
    @FXML
    public  Label playerNameLabel;
    @FXML
    public TextField text1;
    @FXML
    public TextField text2;
    @FXML
    public Button setName;
    private  boolean isAllowedToInsert=true;




    public void createPlayground() {
        Shape rectangleWidthHoles = createGameStructuralGrid();
        rootGrid.add(rectangleWidthHoles, 0, 1);
        List<Rectangle> rectangleList = createClickableColumns();

        for (Rectangle rectangle : rectangleList) {
            rootGrid.add(rectangle,0,1);
        }

    }


    private Shape createGameStructuralGrid(){
        Shape rectangleWidthHoles=new Rectangle((COLUMNS+1)*CIRCLE_DIAMETER,(ROWS+1)*CIRCLE_DIAMETER);
        //here v:width and v1:height

        for(int row=0; row<ROWS;row++){

            for(int col=0; col<COLUMNS;col++){

                Circle circle=new Circle();
                circle.setRadius(CIRCLE_DIAMETER/2);
                circle.setCenterX(CIRCLE_DIAMETER/2);
                circle.setCenterY(CIRCLE_DIAMETER/2);
                circle.setSmooth(true);

                circle.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
                circle.setTranslateY(row*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
                rectangleWidthHoles=Shape.subtract(rectangleWidthHoles,circle);

            }
        }

        rectangleWidthHoles.setFill(Color.WHITE);
        return rectangleWidthHoles;

    }
    private List<Rectangle> createClickableColumns() {

        List<Rectangle> rectangleList=new ArrayList<>();
        for (int col1 = 0; col1 < COLUMNS; col1++) {
            Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col1*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);


            rectangle.setOnMouseEntered(mouseEvent -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(mouseEvent -> rectangle.setFill(Color.TRANSPARENT));

            final int column=col1;
            rectangle.setOnMouseClicked(mouseEvent -> {
                if(input1==null){
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Please Enter the Names");
                    alert.show();
                    return;

                }
                if(isAllowedToInsert) {
                        isAllowedToInsert= false;
                        insertDisc(new Disc(isPlayerOneTurn), column);
                    }

            });

            rectangleList.add(rectangle);
        }
        return rectangleList;
    }

    private void insertDisc(Disc disc,int column) {
        int row=ROWS-1;
        while(row>=0) {
            if (getDiscIfPresent(row,column) == null)
                break;
            row--;
        }
        if(row<0)
            return;
        insertedDiscArray[row][column]=disc;
        insertedDiskPane.getChildren().add(disc);
        disc.setTranslateX(column*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);

        int currentRow=row;
        TranslateTransition transition=new TranslateTransition(Duration.seconds(0.5),disc);
        transition.setToY(row*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);

        transition.setOnFinished(actionEvent -> {
            isAllowedToInsert=true;

            if(gameEnded(currentRow,column)){
                gameOver();
                return;
            }
            isPlayerOneTurn=!isPlayerOneTurn;
            playerNameLabel.setText(isPlayerOneTurn?PLAYER_ONE:PLAYER_TWO);
        });
        transition.play();

    }
    public boolean gameEnded(int row,int column){
        List<Point2D> verticalPoints=
        IntStream.rangeClosed(row-3,row+3) //0 to 5
                .mapToObj(r->new Point2D(r,column)) //0,3 1,3 2,3 3,3
                .collect(Collectors.toList());

            List<Point2D> horizontalPoints=
                IntStream.rangeClosed(column-3,column+3)
                        .mapToObj(col->new Point2D(row,col))
                        .collect(Collectors.toList());

            Point2D startPoint1=new Point2D(row-3,column+3);
            List<Point2D>diagonal1Points=IntStream.rangeClosed(0,6)
                    .mapToObj(i->startPoint1.add(i,-i))
                    .collect(Collectors.toList());


        Point2D startPoint2=new Point2D(row-3,column-3);
        List<Point2D>diagonal2Points=IntStream.rangeClosed(0,6)
                .mapToObj(i->startPoint2.add(i,i))
                .collect(Collectors.toList());



        boolean isEnded=checkCombinations(verticalPoints)||checkCombinations(horizontalPoints)
                ||checkCombinations(diagonal1Points)||checkCombinations(diagonal2Points);
        return isEnded;
    }

    private boolean checkCombinations(List<Point2D> points) {
        int chain = 0;

        for (Point2D point : points) {
            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();

            Disc disc =getDiscIfPresent(rowIndexForArray,columnIndexForArray);
            if (disc != null && disc.isPlayerOneMove == isPlayerOneTurn) {
                chain++;
                if (chain == 4) {
                    return true;
                }
            }else{
                chain = 0;
            }
        }
            return false;

    }

        private Disc getDiscIfPresent(int row, int column){
        if(row>=ROWS ||row<0 || column>=COLUMNS||column<0)
            return null;
        return insertedDiscArray[row][column];
    }

    private void gameOver(){

        String  winner=isPlayerOneTurn ?PLAYER_ONE:PLAYER_TWO;
        System.out.println("Winner is:"+winner);

        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect 4");
        alert.setHeaderText("The Winner is "+winner);
        alert.setContentText("Want to play again?");

        ButtonType yesButton=new ButtonType("Yes");
        ButtonType noButton=new ButtonType("Exit");
        alert.getButtonTypes().setAll(yesButton,noButton);

        Platform.runLater(()->{
            Optional<ButtonType>btnClicked =alert.showAndWait();

            if(btnClicked.isPresent() && btnClicked.get()==yesButton){
                //reset game
                resetGame();

            }else{
                Platform.exit();
                System.exit(0);
                //exit game
            }


        });
    }

    public void resetGame() {
        insertedDiskPane.getChildren().clear();
        for(int row=0;row<insertedDiscArray.length;row++){
            for(int col=0;col<insertedDiscArray[row].length;col++){

                insertedDiscArray[row][col]=null;
            }
        }
        isPlayerOneTurn=true;
       playerNameLabel.setText(PLAYER_ONE);
        createPlayground();


    }

    private static class Disc extends Circle{
        private final boolean isPlayerOneMove;

        public  Disc(boolean isPlayerOneMove){
            this.isPlayerOneMove=isPlayerOneMove;
            setRadius(CIRCLE_DIAMETER/2);
            setFill(isPlayerOneMove?Color.valueOf(discColor1):Color.valueOf(discColor2));
            setCenterX(CIRCLE_DIAMETER/2);
            setCenterY(CIRCLE_DIAMETER/2);
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // System.out.println("Enter Names:");
        setName.setOnAction(actionEvent -> {
            nameset();
        });



    }
    private void nameset() {
        input1=text1.getText();
        input2=text2.getText();
       playerNameLabel.setText(input1);
        PLAYER_ONE=input1;
        PLAYER_TWO=input2;


    }


}





