package service;

import dataAccess.ClearDAO;
import dataAccess.DataAccessException;

import javax.xml.crypto.Data;

public class ClearService {
    private ClearDAO dataAccess;

    public ClearService(ClearDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void ClearAll() throws DataAccessException {
        dataAccess.clearDatabase();
    }
}
