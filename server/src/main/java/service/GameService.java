package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.ResponseData;

public class GameService {
    private GameDAO gameDAO;
    private UserDAO userDAO;

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
        ChessGame oldGame;
        GameData newGameData;

        // Checks auth token
        if (checkAuth == null) {
            return new ResponseData(401, "Error: unauthorized", null, null, null, null);
        }

        // WHITE
        if (playerColor.equals("WHITE")) {
            if (!oldGameData.whiteUsername().isEmpty()) {
                return new ResponseData(403, "Error: already taken", null, null, null, null);
            }
            oldGame = oldGameData.game();
            newGameData = new GameData(gameID, checkAuth.username(), oldGameData.blackUsername(), oldGameData.gameName(), oldGame);
        }

        // BLACK
        else if (playerColor.equals("BLACK")){
            if (!oldGameData.blackUsername().isEmpty()) {
                return new ResponseData(403, "Error: already taken", null, null, null, null);
            }
            oldGame = oldGameData.game();
            newGameData = new GameData(gameID, oldGameData.whiteUsername(), checkAuth.username(), oldGameData.gameName(), oldGame);
        }
        else {
            // Join as observer
            newGameData = oldGameData;
        }

        gameDAO.replaceGame(newGameData);
        return new ResponseData(200, null, null, null, null, null);
    }
}
