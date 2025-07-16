package service;

import dataaccess.DataAccessException;
import dataaccess.SQLDataAccess;
import model.UserData;
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
        UserData CollectedUser = null;
        try {
            CollectedUser = dataAccess.getUser("testuser");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(CollectedUser);
        assertEquals("testuser", CollectedUser.username());

        assertEquals("test@example.com" , CollectedUser.email());
        Assertions.assertTrue(BCrypt.checkpw("password", CollectedUser.password()));
    }
}
