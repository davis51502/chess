package client;
import dataaccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var serverURL = "http://localhost:"+ port;
        serverFacade = new ServerFacade(serverURL);
    }
    @BeforeEach
    public void clear () throws Exception {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void clearDatabase() {
        try {
            serverFacade.clear();
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void registerPositive() {
        AuthData authData = assertDoesNotThrow(() -> serverFacade.
                register("player1", "password", "suibacan@gmail.com"));
        assertNotNull(authData);
        assertEquals("player1", authData.username());
        assertNotNull(authData.authToken());
    }
    @Test
    void registerNegative() throws Exception {
        AuthData frstUser = serverFacade.register("doubled user", "password", "suibacan@gmail.com");
        assertNotNull(frstUser, "1st registration should yield success");
        Exception exception = assertThrows(Exception.class, () -> {
            serverFacade.register("doubled user", "password", "suibacan@gmail.com");
        });
        assertNotNull(exception, "exception thrown for doubled user");

    }
    @Test
    void loginPositive() throws Exception {
        AuthData frstUser = serverFacade.register("user", "password", "suibacan@gmail.com");
        assertNotNull(frstUser, "1st registration should yield success");
        AuthData loginRes = serverFacade.login("user","password");
        assertNotNull(loginRes.authToken(), "login auth shouldn't be null ");
        assertEquals("user", loginRes.username());
    }
    @Test
    void loginNegative() throws Exception {
        Exception exception = assertThrows(Exception.class, () ->
        {serverFacade.login("fake user", "fake password");
        });
        assertNotNull(exception, "exception should be caught for a fake login");
    }
    @Test
    void logoutPositive() throws Exception {
        AuthData frstUser = serverFacade.register("user", "password", "suibacan@gmail.com");
        assertNotNull(frstUser.authToken(), "auth token shouldn't be null");
        assertDoesNotThrow(() -> {serverFacade.logout(frstUser.authToken());
        });
    }
    @Test
    void logoutNegative() throws Exception {
        Exception exception = assertThrows(Exception.class, () ->
        {serverFacade.logout("invalidtoken");});
        assertNotNull(exception, "exception should be thrown because of the invalid token ");

    }





}
