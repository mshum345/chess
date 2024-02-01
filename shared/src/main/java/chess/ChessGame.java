package chess;

import java.lang.reflect.Array;
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
                    validMoves = InCheckMoves(TeamColor.WHITE, TeamColor.BLACK);
                }
                else {
                    LeaveInCheckMoves(testBoard, startPosition, myPiece, validMoves);
                }
            }
            else {
                if (blackCheck) {
                    validMoves = InCheckMoves(TeamColor.BLACK, TeamColor.WHITE);
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
                var tempBoard = new ChessBoard(testBoard);
                tempBoard.TestMove(move, myPiece);
                if (!CheckForCheck(TeamColor.WHITE, TeamColor.BLACK, tempBoard)) {
                    validMoves.add(move);
                }
            }
        }
        // BLACK
        else {
            for (var move : myMoves) {
                // set up variables
                var tempBoard = new ChessBoard(testBoard);
                tempBoard.TestMove(move, myPiece);
                if (!CheckForCheck(TeamColor.BLACK, TeamColor.WHITE, tempBoard)) {
                    validMoves.add(move);
                }
            }
        }
    }
    private ArrayList<ChessMove> GetAllAttackMoves(TeamColor teamColor, ChessBoard testBoard) {
        var attackMoves = new ArrayList<ChessMove>();

        // loop through every testBoard square
        for (var i = 1; i < 9; i++) {
            for (var j = 1; j < 9; j++) {
                var tempPos = new ChessPosition(i, j);
                var tempPiece = testBoard.getPiece(tempPos);
                if (tempPiece != null && tempPiece.getTeamColor() == teamColor) {
                    var tempMoves = new ArrayList<ChessMove>();
                    if (tempPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        tempMoves = tempPiece.PawnAttackMoves(testBoard, tempPos);
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
        if (CheckForCheck(enemyColor, myColor, board)) {
            SetCheck(enemyColor);
            var checkMoves = InCheckMoves(enemyColor, myColor);
            // Checkmate
            if (checkMoves.isEmpty()) {
                SetCheckmate(enemyColor);
            }
        }
        else {
            if (CheckForStalemate(enemyColor, myColor)) {
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

    private boolean CheckForCheck(TeamColor myColor, TeamColor enemyColor, ChessBoard testBoard) {
        var kingPos = testBoard.GetKingPos(myColor);
        var attackMoves = GetAllAttackMoves(enemyColor, testBoard);

        for (var move : attackMoves) {
            var myPos = move.getEndPosition();
            if (myPos.equals(kingPos)) {
                return true;
            }
        }

        return false;
    }

    private boolean CheckForStalemate(TeamColor myColor, TeamColor enemyColor) {
        var staleMoves = new ArrayList<ChessMove>();
        for (var i = 1; i < 9; i++) {
            for (var j = 1; j < 9; j++) {
                var myPos = new ChessPosition(i, j);
                var myPiece = board.getPiece(myPos);
                if (myPiece != null && myColor == myPiece.getTeamColor()) {
                    staleMoves.addAll(validMoves(myPos));
                }
            }
        }
        return staleMoves.isEmpty();
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
        var validMoves = new ArrayList<ChessMove>();

        // Establish team colors
        TeamColor enemyColor;
        TeamColor myColor = myPiece.getTeamColor();
        if (myColor == TeamColor.WHITE) {
            enemyColor = TeamColor.BLACK;
        }
        else {
            enemyColor = TeamColor.WHITE;
        }

        // check for check and fill validmoves
        if (isInCheck(myColor)) {
            validMoves = InCheckMoves(myColor, enemyColor);

        }
        else {
            validMoves = (ArrayList<ChessMove>) validMoves(myPos);
        }

        // Check if valid move and make it
        if (validMoves.contains(move)) {
            // promotion
            if (move.getPromotionPiece() != null) {
                board.addPiece(targPos, new ChessPiece(myColor, move.getPromotionPiece()));
                board.addPiece(myPos, null);
            }
            else {
                board.addPiece(targPos, myPiece);
                board.addPiece(myPos, null);
            }

            // Make any new status changes
            MakeStatusChanges(myColor, enemyColor);
            // Change turns
            this.currentTurn = enemyColor;
        }
        else {
            throw new InvalidMoveException("Invalid Move");
        }
    }

    private ArrayList<ChessMove> InCheckMoves(TeamColor myColor, TeamColor enemyColor) {
        var checkMoves = new ArrayList<ChessMove>();
        var allMyMoves = GetAllMoves(myColor, board);

        for (var move : allMyMoves) {
            var testBoard = new ChessBoard(board);
            var myPiece = testBoard.getPiece(move.getStartPosition());
            testBoard.TestMove(move, myPiece);
            if (!CheckForCheck(myColor, enemyColor, testBoard)) {
                checkMoves.add(move);
            }
        }

        return checkMoves;
    }

    private ArrayList<ChessMove> GetAllMoves(TeamColor teamColor, ChessBoard testBoard) {
        ArrayList<ChessMove> allMoves = new ArrayList<ChessMove>();

        // loop through every testBoard square
        for (var i = 1; i < 9; i++) {
            for (var j = 1; j < 9; j++) {
                var tempPos = new ChessPosition(i, j);
                var tempPiece = testBoard.getPiece(tempPos);
                if (tempPiece != null && tempPiece.getTeamColor() == teamColor) {
                    // get individual piece moves
                    var tempMoves = (ArrayList<ChessMove>) tempPiece.pieceMoves(testBoard, tempPos);
                    // copy tempMoves to attackMoves
                    // adds only unique moves to attackMoves
                    HashSet<ChessMove> temp = new HashSet<>(allMoves);
                    temp.addAll(tempMoves);
                    var temp2 = new ArrayList<>(temp);
                    allMoves.addAll(temp2);
                }
            }
        }
        return allMoves;
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
        MakeStatusChanges(TeamColor.WHITE, TeamColor.BLACK);
        currentTurn = TeamColor.BLACK;
        MakeStatusChanges(TeamColor.BLACK, TeamColor.WHITE);
        currentTurn = TeamColor.WHITE;
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
