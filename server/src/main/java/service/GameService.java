package service;

import dataAccess.GameDAO;

public class GameService {
    private GameDAO dataAccess;

    public GameService(GameDAO dataAccess) {
        this.dataAccess = dataAccess;
    }
}
