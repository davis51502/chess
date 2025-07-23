package client;
import chess.ChessGame;
import dataaccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.Collection;

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
    void loginNegative()  {
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
    void logoutNegative()  {
        Exception exception = assertThrows(Exception.class, () ->
        {serverFacade.logout("invalidtoken");});
        assertNotNull(exception, "exception should be thrown because of the invalid token ");

    }
    @Test
    void createGamePositive() throws Exception {
        AuthData frstUser = serverFacade.register("user", "password", "suibacan@gmail.com");
        assertNotNull(frstUser);
        assertNotNull(frstUser.authToken());
        GameData result = serverFacade.createGame(frstUser.authToken(), "test game");
        assertNotNull(result);
        assertTrue(result.gameID()>0);
    }
    @Test
    void createGameNegative() {
        Exception exception = assertThrows(Exception.class, () ->
        {serverFacade.createGame("invalid", "test game");});
        assertNotNull(exception);
    }
    @Test
    void listGamesPositive() throws Exception {
        AuthData frstUser = serverFacade.register("user", "password", "suibacan@gmail.com");
        assertNotNull(frstUser);
        GameData result = serverFacade.createGame(frstUser.authToken(), "list game");
        assertNotNull(result);
        Collection<GameData> expectations =serverFacade.listGames(frstUser.authToken());
        assertNotNull(expectations);
        assertFalse(expectations.isEmpty());


    }
    @Test
    void listGamesNegative()  {
        Exception exception = assertThrows(Exception.class, () ->
        {serverFacade.listGames("invalid");});
        assertNotNull(exception);
    }
    @Test
    void joinGamesPositive() throws Exception {
        AuthData frstUser = serverFacade.register("user", "password", "suibacan@gmail.com");
        assertNotNull(frstUser);
        GameData result = serverFacade.createGame(frstUser.authToken(), "join game");
        assertNotNull(result);
        assertDoesNotThrow(() ->
        {serverFacade.joinGame(frstUser.authToken(), ChessGame.TeamColor.WHITE, result.gameID());});

    }
    @Test
    void joinGamesNegative()  {
        Exception exception = assertThrows(Exception.class, () ->
        {serverFacade.joinGame("invalid", ChessGame.TeamColor.WHITE, 9999999);});
        assertNotNull(exception);
    }
    @Test
    void clearPositive () throws Exception {
        AuthData frstUser = serverFacade.register("clearuser", "password", "suibacan@gmail.com");
        assertNotNull(frstUser);
        assertDoesNotThrow(() -> {serverFacade.clear();});
        Exception exception = assertThrows(Exception.class, () -> {serverFacade.login("clearuser", "password");});
        assertNotNull(exception);
    }
    @Test
    void clearNegative() {
        ServerFacade bad = new ServerFacade("http://localhost:1234");
        Exception exception = assertThrows(Exception.class, ()-> {bad.clear();});
        assertNotNull(exception);
    }
}
