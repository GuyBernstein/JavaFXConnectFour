import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Arrays;

public class ConnectFourGame {
    private final int SIZE;
    private boolean currentPlayer;
    private Pane[] panes;

    public ConnectFourGame(int size, boolean red, Pane[] panes) {
        this.SIZE = size;
        this.currentPlayer = red;
        this.panes = panes;
    }

    // the logic from handleButton() goes here
    public void handleButtonClick(int columnIndex) {
        // We start from the last row of the pane matrix and check for every row in this column
        int lastPaneRow = SIZE * (SIZE - 2);
        int paneIndex = lastPaneRow + columnIndex;
        int firstRow = 0;
        while (paneIndex >= firstRow && isPaneOccupied(paneIndex))
            paneIndex -= SIZE;
        // Find the bottom row in the selected column
        if (paneIndex < firstRow) { // We went up too much
            // Display an alert dialog
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Column is Full");
            alert.setHeaderText("The column is full.");
            alert.setContentText("Either try another column or press the clear button.");
            alert.showAndWait();
        } else {
            Pane pane = panes[paneIndex];
            Circle circle = getNewCircle(pane);
            pane.getChildren().add(circle);

            if (checkWin(paneIndex)) {
                // Display an alert dialog
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("The game has won!");
                alert.setHeaderText("Congratulations, the player that won is");
                alert.setContentText(currentPlayer ? "red" : "blue");
                alert.showAndWait();
                clearBoard();
            }
            // After the drawing, we switch to the other player
            currentPlayer = !currentPlayer;
        }
    }


    private boolean isPaneOccupied(int index) {
        Pane pane = panes[index];
        for (int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i) instanceof Circle) {
                return true; // A circle is present in the Pane
            }
        }
        return false; // No circle is present in the Pane
    }

    // Creates a new circle with the appropriate size and color based on the current player
    private Circle getNewCircle(Pane pane) {
        double paneWidth = pane.getPrefWidth();
        double paneHeight = pane.getPrefHeight();
        // We divide by 2 to get to the middle point for the circle, and dividing by 2 is to get the radius
        // We multiply the radius by 0.8 to fit the size of the radius to not touch the edges
        int middle = 2;
        double fitSize = 0.8;
        double radius = (Math.min(paneWidth, paneHeight) / middle) * fitSize;
        Circle circle = new Circle(paneWidth / middle, paneHeight / middle, radius);
        // We set the color based on the flag currentPlayer
        circle.setFill(currentPlayer ? Color.RED : Color.BLUE);
        return circle;
    }
    // Checks if there is a winning combination starting from the given index
    private boolean checkWin(int index) {
        int row = index / SIZE; // Calculate the row index
        int col = index % SIZE; // Calculate the column index
        int paneRows = SIZE - 1; // Number of rows in the pane array

        Pane[] horizontal = new Pane[SIZE]; // Array to store the panes in the horizontal direction
        Pane[] vertical = new Pane[SIZE]; // Array to store the panes in the vertical direction
        Pane[] diagonal1 = new Pane[SIZE]; // Array to store the panes in the first diagonal direction
        Pane[] diagonal2 = new Pane[SIZE]; // Array to store the panes in the second diagonal direction

        // Fill the arrays with the panes in the corresponding directions
        for (int i = 0; i < SIZE; i++) {
            int horizontalIndex = row * SIZE + i; // Calculate the index for the horizontal direction
            horizontal[i] = panes[horizontalIndex]; // Store the pane in the horizontal array

            int verticalIndex = i < paneRows ? i * SIZE + col : -1; // Calculate the index for the vertical direction
            vertical[i] = i < paneRows ? panes[verticalIndex] : null; // Store the pane in the vertical array
        }

        // Move the indexes to the right edge for diagonal1
        int diagonal1Row = row;
        int diagonal1Col = col;
        while (diagonal1Col < SIZE - 1 && diagonal1Row > 0) {
            diagonal1Row--; // Move the row index up
            diagonal1Col++; // Move the column index to the right
        }
        // Copy panes into diagonal1 array
        int diagonal1Index = 0;
        while (diagonal1Row < paneRows && diagonal1Col >= 0) {
            diagonal1[diagonal1Index] = panes[diagonal1Row * SIZE + diagonal1Col]; // Store the pane in the diagonal1 array
            diagonal1Index++;
            diagonal1Row++; // Move the row index down
            diagonal1Col--; // Move the column index to the left
        }

        // Move the indexes to the right edge for diagonal2
        int diagonal2Row = row;
        int diagonal2Col = col;
        while (diagonal2Col < SIZE - 1 && diagonal2Row < paneRows - 1) {
            diagonal2Row++; // Move the row index down
            diagonal2Col++; // Move the column index to the right
        }
        // Copy panes into diagonal2 array
        int diagonal2Index = 0;
        while (diagonal2Row >= 0 && diagonal2Col >= 0) {
            diagonal2[diagonal2Index] = panes[diagonal2Row * SIZE + diagonal2Col]; // Store the pane in the diagonal2 array
            diagonal2Index++;
            diagonal2Row--; // Move the row index up
            diagonal2Col--; // Move the column index to the left
        }


        // Check for a 4-connect in each direction
        return checkConnect(horizontal) || checkConnect(vertical) ||
                checkConnect(diagonal1) || checkConnect(diagonal2);

    }
    // Checks if there is a 4-connect in the given array of Panes
    private boolean checkConnect(Pane[] panes) {
        int onePane = 1;
        int zeroPane = 0;
        int fourConnect = 4;
        boolean win = true;
        boolean notWin = false;
        int count = zeroPane; // Counter for the number of connected circles of the same color
        Color currentColor = null; // Current color being checked

        for (Pane pane : panes) {
            if (pane != null) {
                int paneIndex = Arrays.asList(this.panes).indexOf(pane); // Get the index of the current pane
                if (isPaneOccupied(paneIndex)) {
                    Circle circle = getCircleFromPane(pane); // Get the circle from the pane
                    if (circle != null) {
                        Color color = (Color) circle.getFill(); // Get the color of the circle

                        if (currentColor == null) {
                            currentColor = color; // Set the current color to the color of the first circle
                            count = onePane; // Set the count to 1
                        } else if (color.equals(currentColor)) {
                            count++; // Increment the count if the color matches the current color
                            if (count == fourConnect) {
                                return win; // Four connected circles of the same color found
                            }
                        } else {
                            currentColor = color; // Update the current color
                            count = onePane; // Reset the count to 1
                        }
                    } else {
                        currentColor = null; // Reset the current color
                        count = zeroPane; // Reset the count to 0
                    }
                } else {
                    currentColor = null; // Reset the current color
                    count = zeroPane; // Reset the count to 0
                }
            }
        }

        return notWin; // No four connected circles of the same color found
    }
    // Retrieves the circle from a given Pane
    private Circle getCircleFromPane(Pane pane) {
        // Iterate through each child node of the Pane
        for (Node node : pane.getChildren()) {
            // Check if the node is an instance of Circle
            if (node instanceof Circle) {
                // If it is a Circle, cast it to Circle type and return it
                return (Circle) node;
            }
        }
        // If no Circle is found in the Pane, return null
        return null;
    }
    public void clearBoard() {
        // Loop through all the panes and remove the circles
        for (Pane pane : panes) {
            pane.getChildren().removeIf(node -> node instanceof Circle);
        }
    }
}