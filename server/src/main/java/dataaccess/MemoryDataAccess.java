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
    public void createUser(UserData userData)  {
        users.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) {
        try {
            return users.get(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthData createAuth(String username)  {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), username);
        authTokens.put(UUID.randomUUID().toString(), authData);
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken)  {
        return authTokens.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken)  {
        try {
            authTokens.remove(authToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createGame(String gameName)  {
        int id = gameIdCounter++;
        games.put(gameIdCounter++, new GameData(id, null, null, gameName));
        return id;
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames()  {
        return games.values();
    }

    @Override
    public void updateGame(GameData game)  {
        try {
            games.put(game.gameID(), game);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear()  {
        users.clear();
        authTokens.clear();
        games.clear();
        gameIdCounter = 1;
    }
}
