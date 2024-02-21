package service;

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
        var checkAuth = userDAO.getAuth(authData.username());

        if (checkAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        return gameDAO.getGames();
    }

    public GameData createGame(AuthData authData, String gameName) throws DataAccessException {
        return null;
    }
}
