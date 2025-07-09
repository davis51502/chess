package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

//  list result. create, join
public class GameService {
    private DataAccess dataAccess;
    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }
    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        if (dataAccess.getAuth(authToken) == null) {
            throw new DataAccessException("error: unauthorized");
        }
        return dataAccess.listGames();
    }
    public int createGame(String authToken, String name) throws DataAccessException {
        if (dataAccess.getAuth(authToken) == null) {
            throw new DataAccessException("error: unauthorized");
        }
        return dataAccess.createGame(name);
    }
    public void joinGame(String)

}
