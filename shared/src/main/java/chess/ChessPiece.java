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
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        PieceType TESTPromotionPiece = null;

        // up, down, left, right, upL, upR, downL, downR
        // up
        if (row + 1 < 9) {
            var targPos = new ChessPosition(row + 1, col);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                // capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
            }
        }

        // down
        if (row - 1 > 0) {
            var targPos = new ChessPosition(row - 1, col);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
            }
        }

        // left
        if (col - 1 > 0) {
            var targPos = new ChessPosition(row, col - 1);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
            }
        }

        // right
        if (col + 1 < 9) {
            var targPos = new ChessPosition(row, col + 1);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
            }
        }

        // upL
        if (row + 1 < 9 && col - 1 > 0) {
            var targPos = new ChessPosition(row + 1, col - 1);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
            }
        }

        // upR
        if (row + 1 < 9 && col + 1 < 9) {
            var targPos = new ChessPosition(row + 1, col + 1);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
            }
        }

        // downL
        if (row - 1 > 0 && col - 1 > 0) {
            var targPos = new ChessPosition(row - 1, col - 1);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
            }
        }

        // downR
        if (row - 1 > 0 && col + 1 < 9) {
            var targPos = new ChessPosition(row - 1, col + 1);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
            }
        }
    }

    private void KnightMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        PieceType TESTPromotionPiece = null;

        //upL, upR, downL, downR, Lup, Ldown, Rup, Rdown

        // upL
        if (row + 2 < 9 && col - 1 > 0) {
            var targPos = new ChessPosition(row + 2, col - 1);
            var targPiece = board.getPiece(targPos);

            // checks if piece is there
            if (targPiece != null) {
                //capture
                if (targPiece.pieceColor != pieceColor) {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            }
            else {
                validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
            }
        }
    }

    private void PawnMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        PieceType TESTPromotionPiece = null;
        // white on bottom, black on top

        if (pieceColor == ChessGame.TeamColor.WHITE) {
            // white up
            if (row + 1 < 9) {
                var targPos = new ChessPosition(row + 1, col);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece == null) {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                    }
                }
            }
        }

        else {
            // black down
            if (row - 1 > 0) {
                var targPos = new ChessPosition(row - 1, col);
                var targPiece = board.getPiece(targPos);

                // checks if piece is there
                if (targPiece == null) {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
        PieceType TESTPromotionPiece = null;

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
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                        up = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                        down = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                        left = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                        right = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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

    private void RookMoves(ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> validMoves) {
        var up = true;
        var down = true;
        var left = true;
        var right = true;
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        var i = 1;
        PieceType TESTPromotionPiece = null;

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
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                        up = false;
                    }
                } else {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                        down = false;
                    }
                } else {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                        left = false;
                    }
                } else {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
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
                        validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                        right = false;
                    }
                } else {
                    validMoves.add(new ChessMove(myPosition, targPos, TESTPromotionPiece));
                }
            } else {
                right = false;
            }
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
}
