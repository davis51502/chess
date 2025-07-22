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

}
