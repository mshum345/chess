package service;

import dataAccess.ClearDAO;
import dataAccess.DataAccessException;
import model.ResponseData;

public class ClearService {
    private final ClearDAO clearDAO;

    public ClearService(ClearDAO clearDAO) {
        this.clearDAO = clearDAO;
    }

    public ResponseData clearAll() throws DataAccessException {
        clearDAO.clearDatabase();
        return new ResponseData(200, null, null, null, null, null);
    }
}
