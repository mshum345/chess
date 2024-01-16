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
        var validMoves = new ArrayList<ChessMove>();

        if (type == PieceType.BISHOP) {
            BishopMoves(myPosition, board, validMoves);
        }
        else if (type == PieceType.KING) {
            KingMoves(myPosition, board, validMoves);
        }
        else if (type == PieceType.KNIGHT) {
            KnightMoves(myPosition, board, validMoves);
        }
        else if (type == PieceType.PAWN) {
            PawnMoves(myPosition, board, validMoves);
        }
        else if (type == PieceType.QUEEN) {
            QueenMoves(myPosition, board, validMoves);
        }
        else {
            RookMoves(myPosition, board, validMoves);
        }

        return validMoves;
    }

    private void BishopMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {
        var upL = true;
        var upR = true;
        var downL = true;
        var downR = true;
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        var i = 1;
        PieceType TESTPromotionPiece = null;

        while(upL || upR || downL || downR) {
            //upL
            if (upL && row + i < 9 && col - i > 0) {
                // creates new position and piece
                var targPos = new ChessPosition(row + i, col - i);
                var targPiece = board.getPiece(targPos);
                
                // checks if piece is there
                if (targPiece != null) {
                    //blocked
                    if (targPiece.pieceColor == pieceColor) {
                        upL = false;
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                        upL = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                upL = false;
            }

            // upR
            if (upR && row + i < 9 && col + i < 9) {
                // creates new position and piece
                var targPos = new ChessPosition(row + i, col + i);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    //blocked
                    if (targPiece.pieceColor == pieceColor) {
                        upR = false;
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                        upR = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                upR = false;
            }

            // downL
            if (downL && row - i > 0 && col - i > 0) {
                // creates new position and piece
                var targPos = new ChessPosition(row - i, col - i);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    //blocked
                    if (targPiece.pieceColor == pieceColor) {
                        downL = false;
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                        downL = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                downL = false;
            }

            // downR
            if (downR && row - i > 0 && col + i < 9) {
                // creates new position and piece
                var targPos = new ChessPosition(row - i, col + i);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    //blocked
                    if (targPiece.pieceColor == pieceColor) {
                        downR = false;
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                        downR = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                downR = false;
            }
            
            i ++;
        }
    }

    private void KingMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {

    }

    private void KnightMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {
    }

    private void PawnMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {
    }

    private void QueenMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {
    }

    private void RookMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {
    }
}
