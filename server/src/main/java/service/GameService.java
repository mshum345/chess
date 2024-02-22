package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;

public class GameService {
    private GameDAO gameDAO;
    private UserDAO userDAO;

    public GameService(GameDAO gameDAO, UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public Collection listGames(AuthData authData) throws DataAccessException {
        var checkAuth = userDAO.getAuth(authData.authToken());

        if (checkAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        return gameDAO.getGames();
    }

    public GameData createGame(AuthData authData, String gameName) throws DataAccessException {
        var checkAuth = userDAO.getAuth(authData.authToken());

        if (checkAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        return gameDAO.createGame(gameName);
    }

    public void joinGame(AuthData authData, String playerColor, int gameID) throws DataAccessException {
        var checkAuth = userDAO.getAuth(authData.authToken());
        var oldGameData = gameDAO.getGame(gameID);
        ChessGame oldGame;
        GameData newGameData;

        if (checkAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        if (playerColor.equals("WHITE")) {
            if (!oldGameData.WhiteUsername().equals(null)) {
                throw new DataAccessException("Error: already taken");
            }
            oldGame = oldGameData.game();
            newGameData = new GameData(gameID, authData.username(), oldGameData.BlackUsername(), oldGameData.gameName(), oldGame);
        }
        else if (playerColor.equals("BLACK")){
            if (!oldGameData.BlackUsername().equals(null)) {
                throw new DataAccessException("Error: already taken");
            }
            oldGame = oldGameData.game();
            newGameData = new GameData(gameID, oldGameData.WhiteUsername(), authData.username(), oldGameData.gameName(), oldGame);
        }
        else {
            // Join as observer
            newGameData = oldGameData;
        }

        gameDAO.replaceGame(newGameData);
    }
}
