/*
 This is the controller class for the Connect Four app.
*/

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



public class ConnectFourController {

    @FXML
    private GridPane grid; // The GridPane that represents the game board
    private ConnectFourGame game; //The game instance created for implementing the logic
    private final int SIZE = 7; // The size of the game board (7x7)
    private final boolean RED = true; // Constant representing the red player

    private Pane[] panes; // Array to store the Pane objects representing each cell of the game board
    final int RECT_X = 0;
    final int RECT_Y = 0;
    final int PANE_COL_SPAN = 1;
    final int PANE_ROW_SPAN = 1;

    // Initializes the game board and sets up the UI components
    public void initialize() {

        panes = new Pane[SIZE * (SIZE - 1)]; // Initialize the panes array
        Button[] buttons = new Button[SIZE]; // Array to store the buttons for each column
        double cellWidth = grid.getPrefWidth() / SIZE; // Calculate the width of each cell
        double cellHeight = grid.getPrefHeight() / SIZE; // Calculate the height of each cell
        for (int i = 0; i < SIZE * SIZE; i++) {
            int bottomRow = SIZE - 1;
            if (i / SIZE == bottomRow) { // Check if the current index is in the bottom row
                buttons[i % SIZE] = new Button("" + (i % SIZE + 1)); // Create a button for each column
                buttons[i % SIZE].setPrefSize(cellWidth, cellHeight); // Set the size of the button
                buttons[i % SIZE].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        handleButton(e); // Handle button click event
                    }
                });
                //add the button to the grid
                grid.add(buttons[i % SIZE], i % SIZE, bottomRow); // Add the button to the GridPane
            } else {
                panes[i] = new Pane(); // Create a Pane for each cell
                panes[i].setPrefSize(cellWidth, cellHeight); // Set the size of the Pane
                //here we are adding a rectangle to each pane for the visibility of the matrix,
                //                             its part of the ui so im keeping it here
                Rectangle borderRect = new Rectangle(RECT_X, RECT_Y, cellWidth, cellHeight); // Create a rectangular border
                borderRect.setFill(Color.TRANSPARENT); // Set the fill color of the border to transparent
                borderRect.setStroke(Color.BLACK); // Set the stroke color of the border to black
                panes[i].getChildren().add(borderRect); // Add the border to the Pane
                //add the pane to the grid
                grid.add(panes[i], i % SIZE, i / SIZE, PANE_COL_SPAN, PANE_ROW_SPAN); // Add the Pane to the GridPane
            }

        }
        game = new ConnectFourGame(SIZE, RED, panes);//set up the game
    }

    // Handles the button click event when a column is selected
    private void handleButton(ActionEvent e) {
        Button clickedButton = (Button) e.getSource(); // Get the clicked button
        int columnIndex = GridPane.getColumnIndex(clickedButton); // Get the column index of the clicked button
        game.handleButtonClick(columnIndex);//game logic(asked explicitly by Hen to move to another class)
        //We handle the logic in another class to distinguish between the frontend and the backend.
    }

    // Clears the game board by removing all the circles from the Panes
    // Using the logic in game's class
    @FXML
    private void clearPressed() {
        game.clearBoard();
    }
}
