package service;

import dataaccess.DataAccessException;
import dataaccess.SQLDataAccess;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

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

    }
    @Test
    void getUserNegative() {

    }
    @Test
    void verifyPWPositive() {

    }
    @Test
    void verifyPWNegative() {

    }
    @Test
    void createAuthPositive() {

    }
    @Test
    void createAuthNegative() {

    }
    @Test
    void getAuthPositive() {

    }
    @Test
    void getAuthNegative() {

    }
    @Test
    void deleteAuthPositive() {

    }
    @Test
    void deleteAuthNegative() {

    }
    @Test
    void createGamePositive() {

    }
    @Test
    void createGameNegative() {

    }
    @Test
    void getGamePositive() {

    }
    @Test
    void getGameNegative() {

    }
    @Test
    void listGamesPositive() {

    }
    @Test
    void listGamesNegative() {

    }
    @Test
    void updateGamePositive() {

    }
    @Test
    void updateGameNegative() {

    }
    @Test
    public void testClear() throws DataAccessException {
        assertDoesNotThrow(() -> dataAccess.clear());
        assertNull(dataAccess.getUser("anybody"));
        assertTrue(dataAccess.listGames().isEmpty());
    }
}
