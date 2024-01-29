package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
        var up = true;
        var down = true;
        var left = true;
        var right = true;
        var upL = true;
        var upR = true;
        var downL = true;
        var downR = true;
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        var i = 1;

        if (type == PieceType.BISHOP) {
            while(upL || upR || downL || downR) {
                upL = BishopQueenRookMoves(upL, myPosition, board, new ChessPosition(row + i, col - i), validMoves);
                upR = BishopQueenRookMoves(upR, myPosition, board, new ChessPosition(row + i, col + i), validMoves);
                downL = BishopQueenRookMoves(downL, myPosition, board, new ChessPosition(row - i, col - i), validMoves);
                downR = BishopQueenRookMoves(downR, myPosition, board, new ChessPosition(row - i, col + i), validMoves);
                i++;
            }
        }
        else if (type == PieceType.KING) {
            KingMoves(myPosition, board, new ChessPosition(row + 1, col), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row - 1, col), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row, col - 1), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row, col + 1), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row + 1, col - 1), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row + 1, col + 1), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row - 1, col - 1), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row - 1, col + 1), validMoves);
        }
        else if (type == PieceType.KNIGHT) {
            KnightMoves(myPosition, board, new ChessPosition(row + 2, col - 1), validMoves);
            KnightMoves(myPosition, board, new ChessPosition(row + 2, col + 1), validMoves);
            KnightMoves(myPosition, board, new ChessPosition(row - 2, col - 1), validMoves);
            KnightMoves(myPosition, board, new ChessPosition(row - 2, col + 1), validMoves);
            KnightMoves(myPosition, board, new ChessPosition(row + 1, col - 2), validMoves);
            KnightMoves(myPosition, board, new ChessPosition(row - 1, col - 2), validMoves);
            KnightMoves(myPosition, board, new ChessPosition(row + 1, col + 2), validMoves);
            KnightMoves(myPosition, board, new ChessPosition(row - 1, col + 2), validMoves);
        }
        else if (type == PieceType.PAWN) {
            PawnMoves(myPosition, board, validMoves);
        }
        else if (type == PieceType.QUEEN) {
            while(up || down || left || right || upL || upR || downL || downR) {
                up = BishopQueenRookMoves(up, myPosition, board, new ChessPosition(row + i, col), validMoves);
                down = BishopQueenRookMoves(down, myPosition, board, new ChessPosition(row - i, col), validMoves);
                left = BishopQueenRookMoves(left, myPosition, board, new ChessPosition(row, col - i), validMoves);
                right = BishopQueenRookMoves(right, myPosition, board, new ChessPosition(row, col + i), validMoves);
                upL = BishopQueenRookMoves(upL, myPosition, board, new ChessPosition(row + i, col - i), validMoves);
                upR = BishopQueenRookMoves(upR, myPosition, board, new ChessPosition(row + i, col + i), validMoves);
                downL = BishopQueenRookMoves(downL, myPosition, board, new ChessPosition(row - i, col - i), validMoves);
                downR = BishopQueenRookMoves(downR, myPosition, board, new ChessPosition(row - i, col + i), validMoves);
                i++;
            }
        }
        else {
            while(up || down || left || right) {
                up = BishopQueenRookMoves(up, myPosition, board, new ChessPosition(row + i, col), validMoves);
                down = BishopQueenRookMoves(down, myPosition, board, new ChessPosition(row - i, col), validMoves);
                left = BishopQueenRookMoves(left, myPosition, board, new ChessPosition(row, col - i), validMoves);
                right = BishopQueenRookMoves(right, myPosition, board, new ChessPosition(row, col + i), validMoves);
                i++;
            }
        }

        return validMoves;
    }

    private boolean BishopQueenRookMoves(boolean bool, ChessPosition myPosition, ChessBoard board, ChessPosition targPos, ArrayList<ChessMove> validMoves) {
        var targRow = targPos.getRow();
        var targCol = targPos.getColumn();

        if (bool && targRow < 9 && targRow > 0 && targCol < 9 && targCol > 0) {
            // gets target piece
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //blocked
                if (targPiece.pieceColor == pieceColor) {
                    bool = false;
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                    bool = false;
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, null));
            }
        }
        else {
            bool = false;
        }
        return bool;
    }

    private void KingMoves(ChessPosition myPosition, ChessBoard board, ChessPosition targPos, ArrayList<ChessMove> validMoves) {
        var targRow = targPos.getRow();
        var targCol = targPos.getColumn();

        if (targRow < 9 && targRow > 0 && targCol < 9 && targCol > 0) {
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                // capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, null));
            }
        }
    }

    private void KnightMoves(ChessPosition myPosition, ChessBoard board, ChessPosition targPos, ArrayList<ChessMove> validMoves) {
        var targRow = targPos.getRow();
        var targCol = targPos.getColumn();

        if (targRow < 9 && targRow > 0 && targCol < 9 && targCol > 0) {
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                // capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, null));
            }
        }
    }

    private void PawnMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        // white on bottom, black on top

        if (pieceColor == ChessGame.TeamColor.WHITE) {
            // white initial move
            if (row == 2 && board.getPiece(new ChessPosition(row + 2, col)) == null && board.getPiece(new ChessPosition(row + 1, col)) == null) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col), null));
            }

            // white up
            if (row + 1 < 9) {
                var targPos = new ChessPosition(row + 1, col);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece == null) {
                    if (targPos.getRow() == 8) {
                        validMoves.add(new ChessMove(myPosition, targPos, PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, targPos, PieceType.ROOK));
                        validMoves.add(new ChessMove(myPosition, targPos, PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, targPos, PieceType.KNIGHT));
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                    }
                }
            }

            // white upL
            if (row + 1 < 9 && col - 1 > 0) {
                var targPos = new ChessPosition(row + 1, col - 1);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    // capture
                    if (targPiece.pieceColor != pieceColor) {
                        if (targPos.getRow() == 8) {
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.KNIGHT));
                        }
                        else {
                            validMoves.add(new ChessMove(myPosition, targPos, null));
                        }
                    }
                }
            }

            // white upR
            if (row + 1 < 9 && col + 1 < 9) {
                var targPos = new ChessPosition(row + 1, col + 1);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    // capture
                    if (targPiece.pieceColor != pieceColor) {
                        if (targPos.getRow() == 8) {
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.KNIGHT));
                        }
                        else {
                            validMoves.add(new ChessMove(myPosition, targPos, null));
                        }
                    }
                }
            }
        }

        else {
            // black initial move
            if (row == 7 && board.getPiece(new ChessPosition(row - 2, col)) == null && board.getPiece(new ChessPosition(row - 1, col)) == null) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col), null));
            }

            // black down
            if (row - 1 > 0) {
                var targPos = new ChessPosition(row - 1, col);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece == null) {
                    if (targPos.getRow() == 1) {
                        validMoves.add(new ChessMove(myPosition, targPos, PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, targPos, PieceType.ROOK));
                        validMoves.add(new ChessMove(myPosition, targPos, PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, targPos, PieceType.KNIGHT));
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                    }
                }
            }

            // black downL
            if (row - 1 > 0 && col - 1 > 0) {
                var targPos = new ChessPosition(row - 1, col - 1);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    // capture
                    if (targPiece.pieceColor != pieceColor) {
                        if (targPos.getRow() == 1) {
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.KNIGHT));
                        }
                        else {
                            validMoves.add(new ChessMove(myPosition, targPos, null));
                        }
                    }
                }
            }

            // black downR
            if (row - 1 > 0 && col + 1 < 9) {
                var targPos = new ChessPosition(row - 1, col + 1);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    // capture
                    if (targPiece.pieceColor != pieceColor) {
                        if (targPos.getRow() == 1) {
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.KNIGHT));
                        }
                        else {
                            validMoves.add(new ChessMove(myPosition, targPos, null));
                        }
                    }
                }
            }
        }
    }

    public ArrayList<ChessMove> PawnAttackMoves(ChessBoard board, ChessPosition myPosition) {
        var validMoves = new ArrayList<ChessMove>();
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        // white on bottom, black on top

        if (pieceColor == ChessGame.TeamColor.WHITE) {
            // white upL
            if (row + 1 < 9 && col - 1 > 0) {
                var targPos = new ChessPosition(row + 1, col - 1);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    // capture
                    if (targPiece.pieceColor != pieceColor) {
                        if (targPos.getRow() == 8) {
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.KNIGHT));
                        }
                        else {
                            validMoves.add(new ChessMove(myPosition, targPos, null));
                        }
                    }
                }
            }

            // white upR
            if (row + 1 < 9 && col + 1 < 9) {
                var targPos = new ChessPosition(row + 1, col + 1);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    // capture
                    if (targPiece.pieceColor != pieceColor) {
                        if (targPos.getRow() == 8) {
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.KNIGHT));
                        }
                        else {
                            validMoves.add(new ChessMove(myPosition, targPos, null));
                        }
                    }
                }
            }
        }

        else {
            // black downL
            if (row - 1 > 0 && col - 1 > 0) {
                var targPos = new ChessPosition(row - 1, col - 1);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    // capture
                    if (targPiece.pieceColor != pieceColor) {
                        if (targPos.getRow() == 1) {
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.KNIGHT));
                        }
                        else {
                            validMoves.add(new ChessMove(myPosition, targPos, null));
                        }
                    }
                }
            }

            // black downR
            if (row - 1 > 0 && col + 1 < 9) {
                var targPos = new ChessPosition(row - 1, col + 1);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    // capture
                    if (targPiece.pieceColor != pieceColor) {
                        if (targPos.getRow() == 1) {
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, targPos, PieceType.KNIGHT));
                        }
                        else {
                            validMoves.add(new ChessMove(myPosition, targPos, null));
                        }
                    }
                }
            }
        }
        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
