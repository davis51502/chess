package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;

// clear result
public class ClearService {
    private DataAccess dataAccess;
    public ClearService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }
    public void clearApp() throws DataAccessException {
        dataAccess.clear();
    }
}
