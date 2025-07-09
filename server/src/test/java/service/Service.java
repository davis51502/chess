package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Service {
    private MemoryDataAccess dataAccess;
    @BeforeEach
    public void setup() {
        dataAccess = new MemoryDataAccess();
        userService = new UserService(dataAccess);
    }
    @Test
    public void testCreateGetUser() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        dataAccess.createUser(user);

        UserData sui = dataAccess.getUser("test123");
        assertEquals(user.username(), sui.username());
        assertEquals(user.password(), sui.password());
        assertEquals(user.email(), sui.email());
    }
    @Test
    public void testNonCreateUser() throws DataAccessException {
        UserData user = dataAccess.getUser("nonexistent");
        assertNull(user);
    }
    @Test
    public void testRegisterYes() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        AuthData auth =
    }

}
