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

    }
    @Test
    public void testCreateGetUserPositive() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        dataAccess.createUser(user);

        UserData sui = dataAccess.getUser("test123");
        assertEquals(user.username(), sui.username());
        assertEquals(user.password(), sui.password());
        assertEquals(user.email(), sui.email());
    }
    @Test
    public void testCreateUserNegative() throws DataAccessException {
        UserData user = dataAccess.getUser("nonexistent");
        assertNull(user);
    }
    @Test
    public void testRegisterPositive() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        UserService userservice = new UserService(dataAccess);
        AuthData auth = userservice.register(user);
        assertNotNull(auth.authToken());
        assertEquals("test123", auth.username());
    }
    @Test
    public void testRegisterNegative() throws DataAccessException {
        UserData userJuan = new UserData("test123", "test321", "suibacan@gmail.com");
        UserData userTwo = new UserData("test123", "diff_pw", "sui@gmail.com");
        UserService userservice = new UserService(dataAccess);
        userservice.register(userJuan);
        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> {userservice.register(userTwo);});
        assertTrue(exception.getMessage().contains("error: username already exists"));

    }
    @Test
    public void testLoginPositive() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        UserService userservice = new UserService(dataAccess);
        userservice.register(user);
        AuthData auth = userservice.login("test123", "test321");
        assertNotNull(auth.authToken());
        assertEquals("test123", auth.username());
    }
    @Test
    public void testLoginNegative() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        UserService userservice = new UserService(dataAccess);
        userservice.register(user);
        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> {userservice.login("test123", "wrongpw");});
        assertTrue(exception.getMessage().contains("incorrect pw"));
    }
    @Test
    public void testLogoutPositive() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        UserService userservice = new UserService(dataAccess);
        AuthData auth = userservice.register(user);

        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> {userservice.logout(auth.authToken());});
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
    @Test
    public void testLogoutNegative() throws DataAccessException {
        UserService userservice = new UserService(dataAccess);
        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> {userservice.logout("error: missing authentication token");});
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
}
