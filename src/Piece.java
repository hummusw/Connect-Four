////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// File:          Piece.java
// Description:   Pieces for Connect Four
// Author:        Thomas Wang
// Last modified: 01 Jun 2020
// Changelist:
//   01 Jun 2020: Added faded image
//   31 May 2020: Created Piece enum, holding Images for normal and hover
// References:
//
import javafx.scene.image.Image;

public enum Piece {
    RED (0),
    YELLOW (1),
    EMPTY (2);

    // Piece "constants"
    private static Image[] image = new Image[3], hover = new Image[3], fade = new Image[3];
    private static final int IMAGE_SIZE = 200;

    // Piece variables
    private int index;

    /// static initialization //////////////////////////////////////////////////////
    /// Description: Find and save images for Piece
    static {
        image[0] = new Image("images/red.png", IMAGE_SIZE, IMAGE_SIZE, true, true);
        image[1] = new Image("images/yellow.png", IMAGE_SIZE, IMAGE_SIZE, true, true);
        image[2] = new Image("images/empty.png", IMAGE_SIZE, IMAGE_SIZE, true, true);

        hover[0] = new Image("images/redHover.png", IMAGE_SIZE, IMAGE_SIZE, true, true);
        hover[1] = new Image("images/yellowHover.png", IMAGE_SIZE, IMAGE_SIZE, true, true);
        hover[2] = new Image("images/emptyHover.png", IMAGE_SIZE, IMAGE_SIZE, true, true);

        fade[0] = new Image("images/redFade.png", IMAGE_SIZE, IMAGE_SIZE, true, true);
        fade[1] = new Image("images/yellowFade.png", IMAGE_SIZE, IMAGE_SIZE, true, true);
        fade[2] = new Image("images/empty.png", IMAGE_SIZE, IMAGE_SIZE, true, true);
    }

    /// Constructor ////////////////////////////////////////////////////////////////
    /// Description: Creates a Piece and assigns it an index for image getting
    /// Inputs: int index - index of image array to use
    Piece (int index) {
        this.index = index;
    }

    /// getImage ///////////////////////////////////////////////////////////////////
    /// Description: Gets associated image when in the grid
    /// Inputs: none
    /// Output: Image associated with this Piece
    public Image getImage() {
        return image[index];
    }

    /// getHover ///////////////////////////////////////////////////////////////////
    /// Description: Gets associated image when hovering above grid
    /// Inputs: none
    /// Output: Image associated with this Piece
    public Image getHover() {
        return hover[index];
    }

    /// getFade ////////////////////////////////////////////////////////////////////
    /// Description: Gets associated image at game end (when not a winning piece)
    /// Inputs: none
    /// Output: Image associated with this Piece
    public Image getFade() {
        return fade[index];
    }
}
