package service;

import dataAccess.ClearDAO;
import dataAccess.DataAccessException;

public class ClearService {
    private ClearDAO dataAccess;

    public ClearService(ClearDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public boolean ClearAll() throws DataAccessException {
        dataAccess.clearDatabase();

        if (dataAccess.getAllUsers().isEmpty() && dataAccess.getAllAuths().isEmpty() && dataAccess.getAllGames().isEmpty()) {
            return true;
        }
        return false;
    }

}
