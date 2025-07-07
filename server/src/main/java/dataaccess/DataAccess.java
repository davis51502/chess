package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface DataAccess {
    // user methods
    void createUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;

    // auth methods
    AuthData createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;

    // game methods
    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;

    // clear method
    void clear() throws DataAccessException;
}
