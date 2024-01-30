package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentTurn = TeamColor.WHITE;
    private boolean blackStalemate = false;
    private boolean whiteStalemate = false;
    private boolean blackCheckmate = false;
    private boolean whiteCheckmate = false;
    private boolean blackCheck = false;
    private boolean whiteCheck = false;
    private ChessBoard board;


    public ChessGame() {
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        var testBoard = board;
        var myPiece = board.getPiece(startPosition);
        var validMoves = new ArrayList<ChessMove>();


        if (currentTurn == myPiece.getTeamColor()) {
            if (currentTurn == TeamColor.WHITE) {
                if (whiteCheck) {
                    // GetCheckMoves(testBoard, TeamColor.WHITE);
                }
                else {
                    LeaveInCheckMoves(testBoard, startPosition, myPiece, validMoves);
                }
            }
            else {
                if (blackCheck) {
                    // GetCheckMoves(testBoard, TeamColor.BLACK);
                }
                else {
                    LeaveInCheckMoves(testBoard, startPosition, myPiece, validMoves);
                }
            }

        }
        return validMoves;
    }

    private void LeaveInCheckMoves(ChessBoard testBoard, ChessPosition myPosition, ChessPiece myPiece, ArrayList<ChessMove> validMoves) {
        // get validMoves for the start piece
        var myMoves = myPiece.pieceMoves(testBoard, myPosition);

        // move the piece to all valid moves it has and for each move call GetAllAttackMoves() for enemy team pieces
        // WHITE
        if (myPiece.getTeamColor() == TeamColor.WHITE) {
            for (var move : myMoves) {
                // set up variables
                var tempBoard = new ChessBoard();
                tempBoard = testBoard;
                tempBoard.TestMove(move, myPiece);
                var attackMoves = GetAllAttackMoves(TeamColor.BLACK, tempBoard);
                var kingPos = tempBoard.GetKingPos(TeamColor.WHITE);

                // test if invalid move
                var invalidMove = false;
                for (var attackMove : attackMoves) {

                    if (attackMove.getEndPosition() == kingPos) {
                        invalidMove = true;
                        break;
                    }

                }

                // add move
                if (!invalidMove) {
                    validMoves.add(move);
                }
            }
        }
        // BLACK
        else {
            for (var move : myMoves) {
                // set up variables
                var tempBoard = new ChessBoard();
                tempBoard = testBoard;
                tempBoard.TestMove(move, myPiece);
                var attackMoves = GetAllAttackMoves(TeamColor.WHITE, tempBoard);
                var kingPos = tempBoard.GetKingPos(TeamColor.BLACK);

                // test if invalid move
                var invalidMove = false;
                for (var attackMove : attackMoves) {

                    if (attackMove.getEndPosition() == kingPos) {
                        invalidMove = true;
                        break;
                    }

                }

                // add move
                if (!invalidMove) {
                    validMoves.add(move);
                }
            }
        }
    }
    private ArrayList<ChessMove> GetAllAttackMoves(TeamColor teamColor, ChessBoard testBoard) {
        var attackMoves = new ArrayList<ChessMove>();
        ChessPiece currPiece = null;

        // loop through every testBoard square
        for (var i = 1; i < 9; i++) {
            for (var j = 1; j < 9; j++) {
                var tempPos = new ChessPosition(i, j);
                var tempPiece = testBoard.getPiece(tempPos);
                if (tempPiece != null && tempPiece.getTeamColor() == teamColor) {
                    var tempMoves = new ArrayList<ChessMove>();
                    if (tempPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        tempMoves = (ArrayList<ChessMove>) tempPiece.PawnAttackMoves(testBoard, tempPos);
                    }
                    else {
                        // get individual piece moves
                        tempMoves = (ArrayList<ChessMove>) tempPiece.pieceMoves(testBoard, tempPos);
                    }
                    // copy tempMoves to attackMoves
                    // adds only unique moves to attackMoves
                    HashSet<ChessMove> temp = new HashSet<>(attackMoves);
                    temp.addAll(tempMoves);
                    var temp2 = new ArrayList<>(temp);
                    attackMoves.addAll(temp2);
                }
            }
        }
        return attackMoves;
    }

    private void MakeStatusChanges(TeamColor myColor, TeamColor enemyColor) {
        // Check for status changes then make those changes if needed
        if (CheckForCheck(myColor, enemyColor)) {
            SetCheck(enemyColor);
            if (CheckForCheckmate(myColor, enemyColor)) {
                SetCheckmate(enemyColor);
            }
        }
        else {
            if (CheckForStalemate(myColor, enemyColor)) {
                SetStalemate(enemyColor);
            }
        }
    }

    private void SetStalemate(TeamColor enemyColor) {
        if (enemyColor == TeamColor.WHITE) {
            whiteStalemate = true;
        }
        else {
            blackStalemate = true;
        }
    }

    private void SetCheckmate(TeamColor enemyColor) {
        if (enemyColor == TeamColor.WHITE) {
            whiteCheckmate = true;
        }
        else {
            blackCheckmate = true;
        }
    }

    private void SetCheck(TeamColor enemyColor) {
        if (enemyColor == TeamColor.WHITE) {
            whiteCheck = true;
        }
        else {
            blackCheck = true;
        }
    }

    private boolean CheckForCheck(TeamColor myColor, TeamColor enemyColor) {
        return true;
    }

    private boolean CheckForCheckmate(TeamColor myColor, TeamColor enemyColor) {
        return true;
    }

    private boolean CheckForStalemate(TeamColor myColor, TeamColor enemyColor) {
        return true;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var myPos = move.getStartPosition();
        var targPos = move.getEndPosition();
        var myPiece = board.getPiece(myPos);


        // Establish team colors
        TeamColor enemyColor;
        TeamColor myColor = myPiece.getTeamColor();
        if (myColor == TeamColor.WHITE) {
            enemyColor = TeamColor.BLACK;
        }
        else {
            enemyColor = TeamColor.WHITE;
        }

        // Check for Status
        if (isInCheckmate(myColor)) {
            // endGame
        }
        else if (isInStalemate(myColor)) {
            // endGame
        }
        else if (isInCheck(myColor)) {
            // InCheckMoves();
        }
        else {
            var validMoves = validMoves(myPos);
            if (validMoves.contains(move)) {
                board.addPiece(targPos, myPiece);
                board.addPiece(myPos, null);
                // Make any new status changes
                MakeStatusChanges(myColor, enemyColor);
                // Change turns
                this.currentTurn = enemyColor;
            }
            else {
                throw new InvalidMoveException("Invalid Move");
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            return whiteCheck;
        }
        else {
            return blackCheck;
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            return whiteCheckmate;
        }
        else {
            return blackCheckmate;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            return whiteStalemate;
        }
        else {
            return blackStalemate;
        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
