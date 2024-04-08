package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.ResponseData;

public class GameService {
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public GameService(GameDAO gameDAO, UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public ResponseData listGames(AuthData authData) throws DataAccessException {
        var checkAuth = userDAO.getAuth(authData.authToken());

        // Checks auth token
        if (checkAuth == null) {
            return new ResponseData(401, "Error: unauthorized", null, null, null, null);
        }

        var games = gameDAO.getGames();
        return new ResponseData(200, null, null, null, games, null);
    }

    public ResponseData createGame(AuthData authData, String gameName) throws DataAccessException {
        var checkAuth = userDAO.getAuth(authData.authToken());

        // Checks auth token
        if (checkAuth == null) {
            return new ResponseData(401, "Error: unauthorized", null, null, null, null);
        }
        var newGame = gameDAO.createGame(gameName);

        return new ResponseData(200, null, null, null, null, newGame.gameID());
    }

    public ResponseData joinGame(AuthData authData, String playerColor, int gameID) throws DataAccessException {
        var checkAuth = userDAO.getAuth(authData.authToken());
        var oldGameData = gameDAO.getGame(gameID);

        // Checks if bad request
        if (oldGameData == null) {
            return new ResponseData(400, "Error: bad request", null, null, null, null);
        }

        // Checks auth token
        if (checkAuth == null) {
            return new ResponseData(401, "Error: unauthorized", null, null, null, null);
        }

        ChessGame oldGame = oldGameData.game();
        GameData newGameData;

        // Checks if observer
        if (playerColor == null) {
            // join as observer
            return new ResponseData(200, null, null, null, null, null);
        }

        // WHITE
        if (playerColor.equalsIgnoreCase("WHITE")) {
            if (oldGameData.whiteUsername() != null && !oldGameData.whiteUsername().equals(checkAuth.username())) {
                return new ResponseData(403, "Error: already taken", null, null, null, null);
            }

            newGameData = new GameData(gameID, checkAuth.username(), oldGameData.blackUsername(), oldGameData.gameName(), oldGame);
        }

        // BLACK
        else if (playerColor.equalsIgnoreCase("BLACK")) {
            if (oldGameData.blackUsername() != null && !oldGameData.blackUsername().equals(checkAuth.username())) {
                return new ResponseData(403, "Error: already taken", null, null, null, null);
            }
            newGameData = new GameData(gameID, oldGameData.whiteUsername(), checkAuth.username(), oldGameData.gameName(), oldGame);
        }

        else {
            return new ResponseData(200, null, null, null, null, null);
        }

        gameDAO.replaceGame(newGameData);
        return new ResponseData(200, null, null, null, null, null);
    }
}
