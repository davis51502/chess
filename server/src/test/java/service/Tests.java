package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {
    private MemoryDataAccess dataAccess;
    private UserService userService;
    private GameService gameService;

    @BeforeEach
    public void setup() {
        dataAccess = new MemoryDataAccess();
        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);

    }
    @Test
    public void testCreateGetUserPositive() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        dataAccess.createUser(user);

        UserData sui = dataAccess.getUser("test123");
        assertEquals(user.username(), sui.username());

        assertEquals(user.email(), sui.email());
        assertTrue(dataAccess.verifyPw("test123", "test321"));
    }
    @Test
    public void testCreateUserNegative() throws DataAccessException {
        UserData user = dataAccess.getUser("nonexistent");
        assertNull(user);
    }
    // register
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
    // login
    @Test
    public void testLoginPositive() throws DataAccessException {
        dataAccess.clear();
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        UserService userservice = new UserService(dataAccess);
        userservice.register(user);
        AuthData auth = userservice.login("test123", "test321");
        assertNotNull(auth.authToken());
        assertEquals("test123", auth.username());
    }
    @Test
    public void testLoginNegative() throws DataAccessException {
        dataAccess.clear();
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        UserService userservice = new UserService(dataAccess);
        userservice.register(user);
        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> {userservice.login("test123", "wrongpw");});
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
    @Test
    public void testBCryptDirectly() {
        String plainPassword = "test321";
        String storedHash = "$2a$10$zqU2N5n2glor0QFZIQFtdOF9K0ORiSue5qSzqCq6a1iMhsLpyF3nS";

        boolean result = BCrypt.checkpw(plainPassword, storedHash);
        System.out.println("BCrypt check result: " + result);
    }
    // logout
    @Test
    public void testLogoutPositive() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        AuthData auth = userService.register(user);
        userService.logout(auth.authToken());
        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> {userService.logout(auth.authToken());});
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
    @Test
    public void testLogoutNegative() throws DataAccessException {
        UserService userservice = new UserService(dataAccess);
        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> {userservice.logout("error: missing authentication token");});
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
    // list games
    @Test
    public void listGamesPositive() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        AuthData auth = userService.register(user);

        gameService.createGame(auth.authToken(), "test game");
        Collection<GameData> games = gameService.listGames(auth.authToken());
        assertEquals(1, games.size());
        assertTrue(games.stream().anyMatch(game -> "test game".equals(game.gameName())));
    }
    @Test
    public void listGamesNegative() throws DataAccessException {
        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> { gameService.listGames("invalid");});
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
    // create game
    @Test
    public void createGamePositive() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        AuthData auth = userService.register(user);
        int gameID = gameService.createGame(auth.authToken(), "test game");
        assertTrue(gameID >0);
        GameData game= dataAccess.getGame(gameID);
        assertEquals("test game", game.gameName());
        assertNull(game.whiteUsername());
        assertNull(game.blackUsername());

    }
    @Test
    public void createGameNegative() throws DataAccessException {
        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> {gameService.createGame("invalid", "test game");});
        assertTrue(exception.getMessage().contains("error: unauthorized"));
    }
    // join game
    @Test
    public void joinGameWhite() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        AuthData auth = userService.register(user);
        int gameID = gameService.createGame(auth.authToken(), "test game");
        gameService.joinGame(auth.authToken(), "WHITE", gameID);
        GameData game= dataAccess.getGame(gameID);
        assertEquals(auth.username(), game.whiteUsername());
        assertNull(game.blackUsername());

    }
    @Test
    public void joinGameBlack() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        AuthData auth = userService.register(user);
        int gameID = gameService.createGame(auth.authToken(), "test game");
        gameService.joinGame(auth.authToken(), "BLACK", gameID);
        GameData game= dataAccess.getGame(gameID);
        assertNull(game.whiteUsername());
        assertEquals(auth.username(), game.blackUsername());


    }
    @Test
    public void joinGameInvalidColor() throws DataAccessException {
        UserData user = new UserData("test123", "test321", "suibacan@gmail.com");
        AuthData auth = userService.register(user);
        int gameID = gameService.createGame(auth.authToken(), "color test");

        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                gameService.joinGame(auth.authToken(), "MAGENTA", gameID)
        );
        assertTrue(exception.getMessage().contains("error: bad request"));
    }

    // clear
    @Test
    public void testClearPositive() throws DataAccessException {
        dataAccess.createUser(new UserData("test123", "pass", "suibacan@gmail.com"));
        dataAccess.createAuth("test123");
        dataAccess.createGame("Game clear");
        dataAccess.clear();
        assertNull(dataAccess.getUser("test123"));
        assertTrue(dataAccess.listGames().isEmpty());
    }
    @Test
    public void testClearNegative() throws DataAccessException {
        UserData user = new UserData("test123", "pass", "suibacan@gmail.com");
        AuthData auth = userService.register(user);
        gameService.createGame(auth.authToken(), "Game clear");
        dataAccess.clear();

        DataAccessException ex = assertThrows(DataAccessException.class, () -> {
            gameService.listGames(auth.authToken());
        });
        assertTrue(ex.getMessage().contains("unauthorized"));
    }
}
