package chess;

import java.util.ArrayList;
import java.util.Collection;

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
            LeaveInCheckMoves(testBoard, startPosition, myPiece, validMoves);
        }

        return validMoves;
    }

    private void LeaveInCheckMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece, ArrayList<ChessMove> validMoves) {

        var myMoves = myPiece.pieceMoves(board, myPosition);
        // get validMoves for the start piece
        // move the piece to all valid moves it has and for each move call GetAllMoves() for enemy team pieces
        // if the enemy team moves array corresponds with the kings position then dont add valid move

    }

    private void MakeStatusChanges() {
        // Check for check, checkmate, and stalemate then make those changes if needed
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
        // check move against validMoves function's ArrayList
        // if move is contained in validMoves then make the move
        // endPosition = myPiece
        // startPosition = null
        // MakeStatusChanges()
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
