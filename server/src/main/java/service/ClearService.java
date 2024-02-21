package service;

import dataAccess.ClearDAO;
import dataAccess.DataAccessException;

public class ClearService {
    private ClearDAO clearDAO;

    public ClearService(ClearDAO clearDAO) {
        this.clearDAO = clearDAO;
    }

    public void ClearAll() throws DataAccessException {
        clearDAO.clearDatabase();
    }
}
