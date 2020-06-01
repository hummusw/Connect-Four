////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// File:          ActivePlayer.java
// Description:   Players for Connect Four
// Author:        Thomas Wang
// Last modified: 31 May 2020
// Changelist:
//   31 May 2020: Created ActivePlayer enum, holding associated Piece
// References:
//

public enum ActivePlayer {
    RED (Piece.RED),
    YELLOW (Piece.YELLOW),
    NONE (Piece.EMPTY);

    // ActivePlayer variables
    private Piece assignedPiece;

    /// Constructor ////////////////////////////////////////////////////////////////
    /// Description: Creates an ActivePlayer object and gives it a piece
    /// Inputs: Piece piece - assigned Piece for this player
    ActivePlayer (Piece piece) {
        assignedPiece = piece;
    }

    /// getAssignedPiece ///////////////////////////////////////////////////////////
    /// Description: Gets piece assigned to this player
    /// Inputs: none
    /// Output: Piece assigned to this player
    public Piece getAssignedPiece() {
        return assignedPiece;
    }
}
