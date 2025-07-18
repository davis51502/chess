package service;

import dataaccess.DataAccessException;
import dataaccess.SQLDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class SQLTests {
    private SQLDataAccess dataAccess;

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccess = new SQLDataAccess();
        dataAccess.clear(); //clean database everytime
    }
    @AfterEach
    void cleanUp() throws DataAccessException {
        dataAccess.clear();
    }
    @Test
    void createUserPositive() {
        UserData userData = new UserData("testuser", "password", "test@example.com");
        assertDoesNotThrow(() -> dataAccess.createUser(userData));
        UserData collectedUser = null;
        try {
            collectedUser = dataAccess.getUser("testuser");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(collectedUser);
        assertEquals("testuser", collectedUser.username());

        assertEquals("test@example.com" , collectedUser.email());
        Assertions.assertTrue(BCrypt.checkpw("password", collectedUser.password()));
    }
    @Test
    void createUserNegative() {
        UserData userData = new UserData("testuser", "password", "test@example.com");
        assertDoesNotThrow(() -> dataAccess.createUser(userData));
        UserData duplicatedUser = new UserData("testuser", "nuevopassword","different@example.com" );
        DataAccessException exception = assertThrows(DataAccessException.class, () -> dataAccess.createUser(duplicatedUser));
        assertTrue(exception.getMessage().contains("username is already taken"));
    }
    @Test
    void getUserPositive() throws DataAccessException {
        UserData userData = new UserData("testuser", "password", "test@example.com");
        dataAccess.createUser(userData);
        UserData userPerson = dataAccess.getUser("testuser");
        assertNotNull(userPerson);
        assertEquals("testuser", userPerson.username());
        assertEquals("test@example.com", userPerson.email());
    }
    @Test
    void getUserNegative() throws DataAccessException {
        UserData userPerson = dataAccess.getUser("unregistered user");
        assertNull(userPerson);

    }
    @Test
    void verifyPWPositive() throws DataAccessException {
        UserData userData = new UserData("testuser", "password", "test@example.com");
        dataAccess.createUser(userData);
        boolean isOk = dataAccess.verifyPw("testuser", "password");
        assertTrue(isOk);

    }
    @Test
    void verifyPWNegative() throws DataAccessException {
        UserData userData = new UserData("testuser", "password", "test@example.com");
        dataAccess.createUser(userData);
        boolean isOk = dataAccess.verifyPw("testuser", "wrong password");
        assertFalse(isOk);
        boolean isAlright = dataAccess.verifyPw("invalid user", "password");
        assertFalse(isAlright);
    }
    @Test
    void createAuthPositive() throws DataAccessException {
        AuthData authData = dataAccess.createAuth("testuser");
        assertNotNull(authData);
        assertNotNull(authData.authToken());
        assertEquals("testuser", authData.username());

    }
    @Test
    void createAuthNegative() throws DataAccessException {
        AuthData authData = dataAccess.createAuth("null");
        assertNotNull(authData);
        assertNotNull(authData.authToken());

    }
    @Test
    void getAuthPositive() throws DataAccessException {
        AuthData authData = dataAccess.createAuth("testuser");
        AuthData userPerson = dataAccess.getAuth(authData.authToken());
        assertNotNull(userPerson) ;
        assertEquals(authData.authToken(), userPerson.authToken());

    }
    @Test
    void getAuthNegative() throws DataAccessException {
        AuthData userPerson = dataAccess.getAuth("invalid token");
        assertNull(userPerson);

    }
    @Test
    void deleteAuthPositive() throws DataAccessException {
        AuthData createrAuth = dataAccess.createAuth("testuser");
        AuthData userPerson = dataAccess.getAuth(createrAuth.authToken());
        assertNotNull(userPerson);
        assertDoesNotThrow(() -> dataAccess.deleteAuth(createrAuth.authToken()));

    }
    @Test
    void deleteAuthNegative() throws DataAccessException {
        assertDoesNotThrow(() ->  dataAccess.deleteAuth("invalid token"));
    }
    @Test
    void createGamePositive() throws  DataAccessException {
        int gameID = dataAccess.createGame("test game");
        assertTrue(gameID > 0);
        GameData dataGame = dataAccess.getGame(gameID);
        assertNotNull(dataGame);
        assertEquals(gameID, dataGame.gameID());
        assertEquals("test game", dataGame.gameName());

    }
    @Test
    void createGameNegative() throws DataAccessException {
        int gameID = dataAccess.createGame(null);
        assertTrue(gameID > 0);
        GameData dataGame = dataAccess.getGame(gameID);
        assertNotNull(dataGame);
        assertNull(dataGame.gameName());
    }
    @Test
    void getGamePositive() throws DataAccessException {
        int gameID = dataAccess.createGame("test game");
        GameData dataGame = dataAccess.getGame(gameID);
        assertNotNull(dataGame);
        assertEquals(gameID, dataGame.gameID());


    }
    @Test
    void getGameNegative()  throws  DataAccessException{
        GameData dataGame = dataAccess.getGame(123456789);
        assertNull(dataGame);

    }
    @Test
    void listGamesPositive() throws  DataAccessException{
        int game1 = dataAccess.createGame("game1");
        int game2 = dataAccess.createGame("game2");
        int game3 = dataAccess.createGame("game3");
        Collection<GameData> games = dataAccess.listGames();
        assertNotNull(games);
        assertEquals(3, games.size());
    }
    @Test
    void listGamesNegative() throws DataAccessException{
        Collection<GameData> games = dataAccess.listGames();
        assertTrue(games.isEmpty());
    }
    @Test
    void updateGamePositive() throws DataAccessException{
        int gameID = dataAccess.createGame("test game");
        GameData updatedGameplay = new GameData(gameID, "white", "black", "updated game") ;
        assertDoesNotThrow(() -> dataAccess.updateGame(updatedGameplay));
    }
    @Test
    void updateGameNegative() throws  DataAccessException {
        GameData unknownGame = new GameData(123456789, "white", "black", "game");
        assertDoesNotThrow(() -> dataAccess.updateGame(unknownGame));
    }
    @Test
    public void testClear() throws DataAccessException {
        assertDoesNotThrow(() -> dataAccess.clear());
        assertNull(dataAccess.getUser("anybody"));
        assertTrue(dataAccess.listGames().isEmpty());
    }
}
