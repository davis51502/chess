package dataaccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
        if (users.containsKey(userData.username())) {
            throw new DataAccessException("username is already taken");
        }
        String hashedpw = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        UserData userHash = new UserData(userData.username(), hashedpw, userData.email());

        users.put(userData.username(), userHash);
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
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, username);
        authTokens.put(token, authData);
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
        games.put(id, new GameData(id, null, null, gameName));
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

    @Override
    public boolean verifyPw(String username, String normalPassword) throws DataAccessException {
        try {
            UserData user = getUser(username);
            if (user == null) {
                return false;
            }
            return BCrypt.checkpw(normalPassword, user.password());
        } catch (IllegalArgumentException e) {
            throw new DataAccessException("pw verification failed:" + e.getMessage());
        } catch (Exception e) {throw new DataAccessException("error verifying pw: " + e.getMessage()); }
    }
}