package Sudoku;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

        //** Attributs **//
    @FXML
    private Button newGame;
    @FXML
    private ToggleButton nightMode;
    @FXML
    private Label timer;
    @FXML
    private GridPane root;
    @FXML
    private Node focusNode = null;
    @FXML
    private GridPane sudokuGrid;
    @FXML
    private GameBoard sudokuBoard;
    private Stopwatch chrono;
    private boolean enParties = false;

    //** Methodes **//

    public void toggleNightMode(){
        if (nightMode.isSelected()){
            root.getStyleClass().remove("light");
            root.getStyleClass().add("dark");
        } else{
            root.getStyleClass().remove("dark");
            root.getStyleClass().add("light");
        }
    }

    public void newGame(){
        //** Pour abandonner **//
        if(enParties == true){
            this.chrono.stop();
            this.enParties = false;
            displaySolution();
            this.newGame.setText("Nouvelle Partie");
        }else{ //** Pour commencer **//
            this.chrono.reset();
            this.chrono.start();
            this.enParties = true;
            this.newGame.setText("Abandonner");
            sudokuBoard = new GameBoard();
            afficherGrille();
        }
    }

    public void indice(){
        Point p1 = sudokuBoard.randomIndice();
        if (p1 != null) {
            int indice = sudokuBoard.getSolvedCell(p1.getX(), p1.getY());
            Label l = getCell(p1);
            l.setText("" + indice);
            this.sudokuBoard.setCell(indice, p1.getX(), p1.getY());
            if (indice != sudokuBoard.getSolvedCell(p1.getX(), p1.getY())) {
                ((Label) focusNode).getStyleClass().add("erreur");
            } else {
                l.getStyleClass().remove("erreur");
            }
        }
        if (sudokuBoard.isFull()) {
            if (sudokuBoard.isComplete()) {
                win();
            } else {
                displayError();
            }
        }
    }

    public void afficherGrille(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Label l = getCell(new Point(i,j));
                if (sudokuBoard.getCell(i,j) == 0){
                    l.setText("");
                    l.setDisable(false);
                } else{
                    l.setText("" + sudokuBoard.getCell(i,j));
                    l.setDisable(true);
                }
            }
        }
    }

    public Label getCell(Point p1){
        for (Node box : sudokuGrid.getChildren()) {
            for (Node label : ((GridPane)box).getChildren()) {
                Point p2 = getAbsolutePosition(label);
                if (Point.egal(p1, p2)){
                    return (Label)label;
                }
            }
        }
        return null;
    }

    public Point getAbsolutePosition(Node cell){
        int boxColonne = (GridPane.getColumnIndex(cell) == null) ?  0 : (GridPane.getColumnIndex(cell));
        int boxLigne = (GridPane.getRowIndex(cell) == null) ? 0 : (GridPane.getRowIndex(cell));
        int gridColonne = (GridPane.getColumnIndex(cell.getParent()) == null) ?  0 : (GridPane.getColumnIndex(cell.getParent()));
        int gridLigne = (GridPane.getRowIndex(cell.getParent()) == null) ? 0 : (GridPane.getRowIndex(cell.getParent()));
        int y = boxColonne + 3*gridColonne;
        int x = boxLigne + 3*gridLigne;
        return new Point(x,y);
    }

    @FXML
    private void handleOnMouseClicked(MouseEvent event)
    {
        if (focusNode != null){
            focusNode.getStyleClass().remove("selected");
        }
        Node source = (Node)event.getSource();
        focusNode = source;
        focusNode.getStyleClass().add("selected");
    }

    private void numberPressed(int n){
        if(focusNode != null){
            ((Label)focusNode).setText("" + n);
        }
        Point p1 = getAbsolutePosition(focusNode);
        this.sudokuBoard.setCell(n, p1.getX(), p1.getY());
        if (n != sudokuBoard.getSolvedCell(p1.getX(), p1.getY())) {
            ((Label)focusNode).getStyleClass().add("erreur");
        } else{((Label)focusNode).getStyleClass().remove("erreur");
            }

        if (sudokuBoard.isFull()) {
            if (sudokuBoard.isComplete()) {
                win();
            } else {
                displayError();
            }
        }
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case DIGIT1: case NUMPAD1:
                this.numberPressed(1);
                break;
            case DIGIT2: case NUMPAD2:
                this.numberPressed(2);
                break;
            case DIGIT3: case NUMPAD3:
                this.numberPressed(3);
                break;
            case DIGIT4: case NUMPAD4:
                this.numberPressed(4);
                break;
            case DIGIT5: case NUMPAD5:
                this.numberPressed(5);
                break;
            case DIGIT6: case NUMPAD6:
                this.numberPressed(6);
                break;
            case DIGIT7: case NUMPAD7:
                this.numberPressed(7);
                break;
            case DIGIT8: case NUMPAD8:
                this.numberPressed(8);
                break;
            case DIGIT9: case NUMPAD9:
                this.numberPressed(9);
                break;
        }
    }

    private void win() {
        displayWin();
        this.newGame.setText("Nouvelle Partie");
        chrono.stop();
        enParties = false;
    }

    private void displayWin() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        if (nightMode.isSelected()){
            dialog.getDialogPane().getStyleClass().add("dark");
        }
        dialog.getDialogPane().getStylesheets().add("Sudoku/style.css");
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("Sudoku/Icon.png"));
        dialog.getDialogPane().getStyleClass().add("dialog");
        dialog.setTitle("Félicitation");
        dialog.setHeaderText("Vous avez gagné " + chrono.getTime());
        dialog.show();
    }

    private void displayError() {
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        if (nightMode.isSelected()){
            dialog.getDialogPane().getStyleClass().add("dark");
        }
        dialog.getDialogPane().getStylesheets().add("Sudoku/style.css");
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("Sudoku/Icon.png"));
        dialog.getDialogPane().getStyleClass().add("dialog");
        dialog.setTitle("Il y a des erreurs");
        dialog.setHeaderText("Complet mais faux");
        dialog.show();
    }

    private void displaySolution() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                focusNode = this.getCell(new Point(i,j));
                if (this.sudokuBoard.getSolvedBoard()[i][j] !=0) {
                    ((Label)focusNode).setText(""+this.sudokuBoard.getSolvedBoard()[i][j]);
                    focusNode.setDisable(true);
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.chrono = new Stopwatch();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            this.timer.setText("Temps : " + chrono.getTime());
        }));
        timeline.setCycleCount(Timeline. INDEFINITE);
        timeline.play();
    }
}

