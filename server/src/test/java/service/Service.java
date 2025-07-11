package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class Service {
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
    public void testClear() throws DataAccessException {
        dataAccess.createUser(new UserData("test123", "pass", "email1"));
        dataAccess.createAuth("test123");
        dataAccess.createGame("Game 1");
        dataAccess.clear();
        assertNull(dataAccess.getUser("test123"));
        assertTrue(dataAccess.listGames().isEmpty());
    }

    public static void main(String[] args) {
        try {
            // Setup
            MemoryDataAccess dataAccess = new MemoryDataAccess();
            GameService gameService = new GameService(dataAccess);

            // Create user and auth
            UserData user = new UserData("ExistingUser", "password", "email@test.com");
            dataAccess.createUser(user);
            AuthData auth = dataAccess.createAuth("ExistingUser");

            // Create game
            int gameID = gameService.createGame(auth.authToken(), "TestGame");
            System.out.println("Created game with ID: " + gameID);

            // Check initial game state
            GameData initialGame = dataAccess.getGame(gameID);
            System.out.println("Initial game - White: " + initialGame.whiteUsername() + ", Black: " + initialGame.blackUsername());

            // Join as white
            gameService.joinGame(auth.authToken(), "WHITE", gameID);

            // Check updated game state
            GameData updatedGame = dataAccess.getGame(gameID);
            System.out.println("After join - White: " + updatedGame.whiteUsername() + ", Black: " + updatedGame.blackUsername());

            // Check list games
            Collection<GameData> games = gameService.listGames(auth.authToken());
            for (GameData game : games) {
                System.out.println("Listed game - ID: " + game.gameID() + ", White: " + game.whiteUsername() + ", Black: " + game.blackUsername());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
