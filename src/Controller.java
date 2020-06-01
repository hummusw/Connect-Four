////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// File:          Controller.java
// Description:   Controller for Connect Four
// Author:        Thomas Wang
// Last modified: 01 Jun 2020
// Changelist:
//   01 Jun 2020: Implemented more functionality (start new game, select starting player, undo move, show game end)
//   31 May 2020: Implemented basic functionality (show hover, place pieces, switch players, determine game end)
// References:
//

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Random;

/// Controller /////////////////////////////////////////////////////////////////
/// Description: Controller for Connect Four
public class Controller {
    // JavaFX objects
    public VBox col1, col2, col3, col4, col5, col6, col7;
    public Button newGameButton, undoButton;
    public ChoiceBox<String> startingPlayerChoice;

    // Game constants
    private static final int NUMBER_ROWS = 6, NUMBER_COLS = 7;
    private static final int CHECK_HORIZ = 0, CHECK_VERTI = 1, CHECK_DIAG1 = 2, CHECK_DIAG2 = 3;
    private static final int[][] CHECK_OFFSETS = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
    private static final String DEFAULT_START = "Random";
    private static final int MAX_TURNS = 42;

    // Game variables
    private ActivePlayer activePlayer = nextStartingPlayer(DEFAULT_START);
    private ActivePlayer lastStartingPlayer = activePlayer;
    private Piece[][] grid = {{Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY},
                              {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY},
                              {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY},
                              {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY},
                              {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY},
                              {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY}};
    private int turnCount = 0;
    private boolean gameOver = false;
    private boolean[][] winPieces = {{false, false, false, false, false, false, false},
                                     {false, false, false, false, false, false, false},
                                     {false, false, false, false, false, false, false},
                                     {false, false, false, false, false, false, false},
                                     {false, false, false, false, false, false, false},
                                     {false, false, false, false, false, false, false}};
    private ActivePlayer lastPlayer = ActivePlayer.NONE;
    private int lastCol = -1;
    private VBox lastVBox = null;

    /// columnEnter ////////////////////////////////////////////////////////////////
    /// Description: Shows the hover piece for a column, called for onMouseEntered
    /// Inputs: MouseEvent mouseEvent - Initiating mouse event
    /// Output: none
    public void columnEnter(MouseEvent mouseEvent) {
        // The ImageView for showing the hover piece is always at index 0
        VBox selected = (VBox) mouseEvent.getSource();
        ImageView imageView = (ImageView) selected.getChildren().get(0);
        imageView.setImage(activePlayer.getAssignedPiece().getHover());
    }

    /// columnLeave ////////////////////////////////////////////////////////////////
    /// Description: Hides the hover piece for a column, called for onMouseExited
    /// Inputs: MouseEvent mouseEvent - Initiating mouse event
    /// Output: none
    public void columnLeave(MouseEvent mouseEvent) {
        // The ImageView for showing the hover piece is always at index 0
        VBox selected = (VBox) mouseEvent.getSource();
        ImageView imageView = (ImageView) selected.getChildren().get(0);
        imageView.setImage(Piece.EMPTY.getHover());
    }

    /// columnSelect ///////////////////////////////////////////////////////////////
    /// Description: Places a piece in a column, callled for onMouseClicked
    /// Inputs: MouseEvent mouseEvent - Initiating mouse event
    /// Output: none
    public void columnSelect(MouseEvent mouseEvent) {
        // Do nothing if game is over
        if (gameOver) {
            return;
        }

        // Find selected column
        VBox selected = (VBox) mouseEvent.getSource();
        HBox hbox = (HBox) selected.getParent();
        int column = hbox.getChildren().indexOf(selected);

        // Find where the piece would fall to (place at row, check rowPlusOne for empty)
        int row = -1, rowPlusOne = 0;
        while (row < NUMBER_ROWS - 1) {
            // Stop when a non-empty piece has been found (stops the fall)
            if (grid[rowPlusOne][column] != Piece.EMPTY) {
                break;
            }

            // Otherwise, continue to fall
            row++;
            rowPlusOne++;
        }

        // If the column is full, do nothing
        if (row == -1) {
            return;
        }

        // Update grid and display
        ImageView imageView = (ImageView) selected.getChildren().get(row+1);
        imageView.setImage(activePlayer.getAssignedPiece().getImage());
        grid[row][column] = activePlayer.getAssignedPiece();

        // Update other game variables
        lastPlayer = activePlayer;
        lastCol = column;
        lastVBox = selected;
        undoButton.setDisable(false);
        turnCount++;

        // Check if the game won, game drawn, or if it's the next player's turn
        gameOver = checkWin(row, column, activePlayer.getAssignedPiece()); //fixme better variable name
        if (gameOver) {
            activePlayer = ActivePlayer.NONE;
            newGameButton.setDefaultButton(true);

            // Fade all non-winning pieces
            for (int c = 0; c < NUMBER_COLS; c++) {
                VBox vBox = (VBox) hbox.getChildren().get(c);
                ObservableList<Node> children = vBox.getChildren();

                for (int r = 0; r < NUMBER_ROWS; r++) {
                    ImageView iv = (ImageView) children.get(r + 1);
                    iv.setImage(winPieces[r][c] ? grid[r][c].getImage() : grid[r][c].getFade());
                }
            }
        } else if (turnCount == MAX_TURNS){
            gameOver = true;
            activePlayer = ActivePlayer.NONE;
            newGameButton.setDefaultButton(true);

            // Fade all pieces
            for (int c = 0; c < NUMBER_COLS; c++) {
                VBox vBox = (VBox) hbox.getChildren().get(c);
                ObservableList<Node> children = vBox.getChildren();

                for (int r = 0; r < NUMBER_ROWS; r++) {
                    ImageView iv = (ImageView) children.get(r + 1);
                    iv.setImage(grid[r][c].getFade());
                }
            }
        } else {
            switch (activePlayer) {
                case RED:
                    activePlayer = ActivePlayer.YELLOW;
                    break;
                case YELLOW:
                    activePlayer = ActivePlayer.RED;
                    break;
            }
        }

        // Update hover piece
        columnEnter(mouseEvent);

        // Remove focus from side panel
        selected.requestFocus();
    }

    /// checkWin ///////////////////////////////////////////////////////////////////
    /// Description: Checks if the game has been won
    /// Inputs: int row, int col - Coordinates of most recently played piece
    ///         Piece piece - color to check for
    /// Output: true if game has been won, false if not
    private boolean checkWin(int row, int col, Piece piece) {
        // Check all four possible directions (done separately to avoid short-circuiting)
        boolean horizontalWin = checkDirection(row, col, piece, CHECK_HORIZ),
                verticalWin   = checkDirection(row, col, piece, CHECK_VERTI),
                diagonal1Win  = checkDirection(row, col, piece, CHECK_DIAG1),
                diagonal2Win  = checkDirection(row, col, piece, CHECK_DIAG2);
        return horizontalWin || verticalWin || diagonal1Win || diagonal2Win;
    }

    /// checkDirection /////////////////////////////////////////////////////////////
    /// Description: Checks a direction for four or more consecutive pieces
    /// Inputs: int row, int col - Coordinates of most recently played piece
    ///         Piece piece - color to check for
    ///         int direction - the direction to check in
    /// Output: true if four or more consecutive pieces found, else false
    private boolean checkDirection(int row, int col, Piece piece, int direction) {
        // Find offsets (row, col) from list
        int[] offsets = CHECK_OFFSETS[direction];

        // Offsets of winning pieces
        int[] winningOffsets = {0, 0, 0, 0, 0, 0, 0};

        // Number of consecutive pieces found (most recently played counts as first)
        int consecutive = 1;

        // Check in the negative direction, continuing as long as same color found
        for (int i = -1; inBounds(row + i * offsets[0], col + i * offsets[1]); i--) {
            if (grid[row + i * offsets[0]][col + i * offsets[1]] == piece) {
                winningOffsets[7 + i] = i;
                consecutive++;
            } else {
                break;
            }
        }

        // Check in the positive direction, continuing as long as same color found
        for (int i = 1; inBounds(row + i * offsets[0], col + i * offsets[1]); i++) {
            if (grid[row + i * offsets[0]][col + i * offsets[1]] == piece) {
                winningOffsets[i] = i;
                consecutive++;
            } else {
                break;
            }
        }

        // Four or more counts as a win (and if so, mark pieces as winning pieces)
        if (consecutive >= 4) {
            for (int offset : winningOffsets) {
                winPieces[row + offset * offsets[0]][col + offset * offsets[1]] = true;
            }
            return true;
        } else {
            return false;
        }
    }

    /// inBounds ///////////////////////////////////////////////////////////////////
    /// Description: Checks if a location is valid
    /// Inputs: int row, int col - Coordinates of location to check
    /// Output: true if location is in bounds, else false
    private boolean inBounds(int row, int col) {
        return 0 <= row && row < NUMBER_ROWS && 0 <= col && col < NUMBER_COLS;
    }

    /// requestFocus ///////////////////////////////////////////////////////////////
    /// Description: Requests focus (used by side panel to remove current focus)
    /// Inputs: MouseEvent mouseEvent - Initiating mouse event
    /// Output: none
    public void requestFocus(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        vBox.requestFocus();
    }

    /// newGame ////////////////////////////////////////////////////////////////////
    /// Description: Starts a new game of connect four
    /// Inputs: none
    /// Output: none
    public void newGame() {
        // Empty out grid of pieces and winning pieces
        for (int i = 0; i < NUMBER_ROWS; i++) {
            for (int j = 0; j < NUMBER_COLS; j++) {
                grid[i][j] = Piece.EMPTY;
                winPieces[i][j] = false;
            }
        }

        // Clear graphical display
        clearColumn(col1);
        clearColumn(col2);
        clearColumn(col3);
        clearColumn(col4);
        clearColumn(col5);
        clearColumn(col6);
        clearColumn(col7);

        // Set up game variables
        activePlayer = nextStartingPlayer(startingPlayerChoice.getValue());
        lastPlayer = ActivePlayer.NONE;
        lastCol = -1;
        lastVBox = null;
        gameOver = false;
        turnCount = 0;

        // Update buttons
        newGameButton.setDefaultButton(false);
        undoButton.setDisable(true);
    }

    /// clearColumn ////////////////////////////////////////////////////////////////
    /// Description: Sets a column to all empty pieces (used by newGame)
    /// Inputs: VBox vbox - the column to clear out
    /// Output: none
    private void clearColumn(VBox vbox) {
        for (int index = 1; index < NUMBER_ROWS + 1; index++) {
            ImageView imageView = (ImageView) vbox.getChildren().get(index);
            imageView.setImage(Piece.EMPTY.getImage());
        }
    }

    /// undoMove ///////////////////////////////////////////////////////////////////
    /// Description: Undoes the most recent move
    /// Inputs: none
    /// Output: none
    public void undoMove() {
        // If there isn't enough information to undo, do nothing
        if (lastPlayer == ActivePlayer.NONE || lastCol == -1 || lastVBox == null) {
            return;
        }

        // If we have to reset the gaveOver status
        if (gameOver) {
            gameOver = false;

            // Clear out grid of winning pieces
            for (int i = 0; i < NUMBER_ROWS; i++) {
                for (int j = 0; j < NUMBER_COLS; j++) {
                    winPieces[i][j] = false;
                }
            }

            // Set up a fake HBox
            VBox[] hbox = {col1, col2, col3, col4, col5, col6, col7};

            // Unfade all pieces
            for (int c = 0; c < NUMBER_COLS; c++) {
                VBox vBox = hbox[c];
                ObservableList<Node> children = vBox.getChildren();

                for (int r = 0; r < NUMBER_ROWS; r++) {
                    ImageView iv = (ImageView) children.get(r + 1);
                    iv.setImage(grid[r][c].getImage());
                }
            }
        }

        // Find the row of the most recent piece
        int row = 0;
        while (row < NUMBER_ROWS) {
            if (grid[row][lastCol] != Piece.EMPTY) {
                break;
            }
            row++;
        }

        // Remove piece and update display
        grid[row][lastCol] = Piece.EMPTY;
        ImageView imageView = (ImageView) lastVBox.getChildren().get(row + 1);
        imageView.setImage(Piece.EMPTY.getImage());

        // Update game variables
        activePlayer = lastPlayer;
        lastPlayer = ActivePlayer.NONE;
        lastCol = -1;
        turnCount--;

        // Update buttons and remove focus
        newGameButton.setDefaultButton(false);
        undoButton.setDisable(true);
        col1.requestFocus();
    }

    /// nextStartingPlayer /////////////////////////////////////////////////////////
    /// Description: Determines who should start the next game
    /// Inputs: String algorithm - algorithm to use
    /// Output: ActivePlayer that starts the game
    private ActivePlayer nextStartingPlayer(String algorithm) {
        switch (algorithm) {
            case "Red":
                lastStartingPlayer = ActivePlayer.RED;
                break;
            case "Yellow":
                lastStartingPlayer = ActivePlayer.YELLOW;
                break;
            case "Switch":
                lastStartingPlayer = lastStartingPlayer == ActivePlayer.RED ? ActivePlayer.YELLOW : ActivePlayer.RED;
                break;
            case "Random":
                lastStartingPlayer = new Random().nextBoolean() ? ActivePlayer.RED : ActivePlayer.YELLOW;
                break;
        }
        return lastStartingPlayer;
    }
}
