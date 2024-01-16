package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.BISHOP) {
            bishopMoves(myPosition);
        }

        return validMoves;
    }

    private void bishopMoves(ChessPosition myPosition) {
        boolean upL = true;
        boolean upR = true;
        boolean downL = true;
        boolean downR = true;
        int i = 1;
        PieceType TESTPromotionPiece = PieceType.QUEEN;

        while(upL || upR || downL || downR) {
            if (myPosition.getRow() + i < 9 && myPosition.getColumn() - i > 0) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i), TESTPromotionPiece));
            }
            else {
                upL = false;
            }

            if (myPosition.getRow() + i < 9 && myPosition.getColumn() + i < 9) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i), TESTPromotionPiece));
            }
            else {
                upR = false;
            }

            if (myPosition.getRow() - i > 0 && myPosition.getColumn() - i > 0) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i), TESTPromotionPiece));
            }
            else {
                downL = false;
            }

            if (myPosition.getRow() - i > 0 && myPosition.getColumn() + i < 9) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i), TESTPromotionPiece));
            }
            else {
                downR = false;
            }
            
            i ++;
        }

        return;
    }
}
