package service;

import dataAccess.ClearDAO;
import dataAccess.DataAccessException;
import model.ResponseData;

public class ClearService {
    private ClearDAO clearDAO;

    public ClearService(ClearDAO clearDAO) {
        this.clearDAO = clearDAO;
    }

    public ResponseData ClearAll() throws DataAccessException {
        clearDAO.clearDatabase();
        return new ResponseData(200, null, null, null, null, 0);
    }
}
