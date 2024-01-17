package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];

        // Add pawns
        for (int i = 1; i < 9; i++) {
            addPiece(new ChessPosition(2,i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7,i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

            // Add all other pieces
            switch (i) {
                // Add rooks
                case 1:
                    addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                    addPiece(new ChessPosition(1,i + 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                    addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
                    addPiece(new ChessPosition(8,i + 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
                    break;
                // Add knights
                case 2:
                    addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
                    addPiece(new ChessPosition(1,i + 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
                    addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
                    addPiece(new ChessPosition(8,i + 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
                    break;
                // Add bishops
                case 3:
                    addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
                    addPiece(new ChessPosition(1,i + 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
                    addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
                    addPiece(new ChessPosition(8,i + 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
                    break;
                // Add queens
                case 4:
                    addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
                    addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
                    break;
                // Add kings
                case 5:
                    addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
                    addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
                    break;
            }
        }


    }
}
