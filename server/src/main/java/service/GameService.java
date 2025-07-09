package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

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
    public void joinGame(String authToken, String gamerColor, int gameID) throws DataAccessException {
        // check if user is authorized
        if (dataAccess.getAuth(authToken) == null) {
            throw new DataAccessException("error: unauthorized");
        }
        // check if the game exists
        if (dataAccess.getGame(gameID) == null) {
            throw new DataAccessException("can't do that: invalid request");
        }
        // check which side the player wants to join: black or white
        if (gamerColor.equalsIgnoreCase("WHITE")) {
            if (dataAccess.getGame(gameID).whiteUsername() != null) {
                throw new DataAccessException("error: already in use ");
            }
            GameData gameUpdate = new GameData(gameID, dataAccess.getAuth(authToken).username(),
                    dataAccess.getGame(gameID).blackUsername(), dataAccess.getGame(gameID).gameName());
            dataAccess.updateGame(gameUpdate);
        } else if (gamerColor.equalsIgnoreCase("BLACK")) {
            if (dataAccess.getGame(gameID).whiteUsername() != null) {
                throw new DataAccessException("error: already in use ");
            }
            GameData gameUpdate = new GameData(gameID, dataAccess.getAuth(authToken).username(),
                    dataAccess.getGame(gameID).whiteUsername(), dataAccess.getGame(gameID).gameName());
            dataAccess.updateGame(gameUpdate);

    }

}
}
