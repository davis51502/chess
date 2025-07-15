package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.*;

public class MemoryDataAccess implements DataAccess {
    private Map<String, UserData> users;
    private Map<String, AuthData> authTokens;
    private Map<Integer, GameData> games;
    private int gameIdCounter;

    public MemoryDataAccess() {
        users = new HashMap<>(); 
        authTokens = new HashMap<>(); 
        games = new HashMap<>();
        gameIdCounter = 1;
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        users.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try {
            return users.get(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException  {
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, username);
        authTokens.put(token, authData);
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException  {
        return authTokens.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException  {
        try {
            authTokens.remove(authToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createGame(String gameName)  throws DataAccessException {
        int id = gameIdCounter++;
        games.put(id, new GameData(id, null, null, gameName));
        return id;
    }

    @Override
    public GameData getGame(int gameID)  throws DataAccessException{
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames()  throws DataAccessException {
        return games.values();
    }

    @Override
    public void updateGame(GameData game)  throws DataAccessException {
        try {
            games.put(game.gameID(), game);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear()  throws DataAccessException {
        users.clear();
        authTokens.clear();
        games.clear();
        gameIdCounter = 1;
    }
}
