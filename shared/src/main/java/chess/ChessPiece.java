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
                upL = BishopMoves(upL, myPosition, board, new ChessPosition(row + i, col - i), validMoves);
                upR = BishopMoves(upR, myPosition, board, new ChessPosition(row + i, col + i), validMoves);
                downL = BishopMoves(downL, myPosition, board, new ChessPosition(row - i, col - i), validMoves);
                downR = BishopMoves(downR, myPosition, board, new ChessPosition(row - i, col + i), validMoves);
                i++;
            }
        }
        else if (type == PieceType.KING) {
            KingMoves(myPosition, board, new ChessPosition(row + i, col), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row - i, col), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row, col - i), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row, col + i), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row + i, col - i), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row + i, col + i), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row - i, col - i), validMoves);
            KingMoves(myPosition, board, new ChessPosition(row - i, col + i), validMoves);
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

    private boolean BishopMoves(boolean bool, ChessPosition myPosition, ChessBoard board, ChessPosition targPos, ArrayList<ChessMove> validMoves) {
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

    private void KnightMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {
        var row = myPosition.getRow();
        var col = myPosition.getColumn();

        //upL, upR, downL, downR, Lup, Ldown, Rup, Rdown

        // upL
        if (row + 2 < 9 && col - 1 > 0) {
            var targPos = new ChessPosition(row + 2, col - 1);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, null));
            }
        }

        // upR
        if (row + 2 < 9 && col + 1 < 9) {
            var targPos = new ChessPosition(row + 2, col + 1);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, null));
            }
        }

        // downL
        if (row - 2 > 0 && col - 1 > 0) {
            var targPos = new ChessPosition(row - 2, col - 1);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, null));
            }
        }

        // downR
        if (row - 2 > 0 && col + 1 < 9) {
            var targPos = new ChessPosition(row - 2, col + 1);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, null));
            }
        }

        // Lup
        if (row + 1 < 9 && col - 2 > 0) {
            var targPos = new ChessPosition(row + 1, col - 2);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, null));
            }
        }

        // Ldown
        if (row - 1 > 0 && col - 2 > 0) {
            var targPos = new ChessPosition(row - 1, col - 2);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, null));
            }
        }

        // Rup
        if (row + 1 < 9 && col + 2 < 9) {
            var targPos = new ChessPosition(row + 1, col + 2);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, null));
            }
        }

        // Rdown
        if (row - 1 > 0 && col + 2 < 9) {
            var targPos = new ChessPosition(row - 1, col + 2);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
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

    private void QueenMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {
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

        while(up || down || left || right || upL || upR || downL || downR) {
            //up
            if (up && row + i < 9) {
                // creates new position and piece
                var targPos = new ChessPosition(row + i, col);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    //blocked
                    if (targPiece.pieceColor == pieceColor) {
                        up = false;
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                        up = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                up = false;
            }

            //down
            if (down && row - i > 0) {
                // creates new position and piece
                var targPos = new ChessPosition(row - i, col);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    //blocked
                    if (targPiece.pieceColor == pieceColor) {
                        down = false;
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                        down = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                down = false;
            }

            //left
            if (left && col - i > 0) {
                // creates new position and piece
                var targPos = new ChessPosition(row, col - i);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    //blocked
                    if (targPiece.pieceColor == pieceColor) {
                        left = false;
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                        left = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                left = false;
            }

            //right
            if (right && col + i < 9) {
                // creates new position and piece
                var targPos = new ChessPosition(row, col + i);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    //blocked
                    if (targPiece.pieceColor == pieceColor) {
                        right = false;
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                        right = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                right = false;
            }

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
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                        upL = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
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
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                        upR = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
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
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                        downL = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
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
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                        downR = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            }
            else {
                downR = false;
            }

            i ++;
        }
    }

    private void RookMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {
        var up = true;
        var down = true;
        var left = true;
        var right = true;
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        var i = 1;

        while(up || down || left || right) {
            //up
            if (up && row + i < 9) {
                // creates new position and piece
                var targPos = new ChessPosition(row + i, col);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    //blocked
                    if (targPiece.pieceColor == pieceColor) {
                        up = false;
                    } else {
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                        up = false;
                    }
                } else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            } else {
                up = false;
            }

            //down
            if (down && row - i > 0) {
                // creates new position and piece
                var targPos = new ChessPosition(row - i, col);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    //blocked
                    if (targPiece.pieceColor == pieceColor) {
                        down = false;
                    } else {
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                        down = false;
                    }
                } else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            } else {
                down = false;
            }

            //left
            if (left && col - i > 0) {
                // creates new position and piece
                var targPos = new ChessPosition(row, col - i);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    //blocked
                    if (targPiece.pieceColor == pieceColor) {
                        left = false;
                    } else {
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                        left = false;
                    }
                } else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            } else {
                left = false;
            }

            //right
            if (right && col + i < 9) {
                // creates new position and piece
                var targPos = new ChessPosition(row, col + i);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece != null) {
                    //blocked
                    if (targPiece.pieceColor == pieceColor) {
                        right = false;
                    } else {
                        validMoves.add(new ChessMove(myPosition, targPos, null));
                        right = false;
                    }
                } else {
                    validMoves.add(new ChessMove(myPosition, targPos, null));
                }
            } else {
                right = false;
            }
            i++;
        }
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
