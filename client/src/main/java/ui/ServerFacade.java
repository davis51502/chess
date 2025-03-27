package ui;

import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ServerFacade provides a simplified interface for interacting with the chess server.
 * It handles HTTP connections for user authentication, game management, and server operations.
 */
public class ServerFacade {
    // Default server configuration
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_URL = "http://localhost";

    // Instance configuration
    private int port;
    private String serverUrl;
    private String authToken;

    // Gson instance for JSON serialization/deserialization
    private final Gson gson = new Gson();

    /**
     * Constructs a ServerFacade with custom server URL and port.
     *
     * @param serverUrl Base URL of the server
     * @param port Port number for server connection
     */
    public ServerFacade(String serverUrl, int port) {
        this.port = (port > 0) ? port : DEFAULT_PORT;
        this.serverUrl = (serverUrl != null && !serverUrl.isEmpty()) ? serverUrl : DEFAULT_URL;
        this.authToken = null;
    }

    /**
     * Clears the entire server database.
     *
     * @return true if database was successfully cleared, false otherwise
     * @throws Exception if there's a network or server error
     */
    public boolean clearDatabase() throws Exception {
        HttpURLConnection http = createHttpConnection("/db", "DELETE");
        int status = http.getResponseCode();
        return status >= 200 && status < 300;
    }

    /**
     * Registers a new user on the server.
     *
     * @param user User data for registration
     * @throws Exception if registration fails due to various reasons
     */
    public void register(UserData user) throws Exception {
        HttpURLConnection http = createHttpConnection("/user", "POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        sendJsonBody(http, user);
        http.connect();

        int status = http.getResponseCode();
        if (status >= 200 && status < 300) {
            getAuthToken(http);
        } else {
            handleRegistrationError(status);
        }
    }

    /**
     * Logs in an existing user.
     *
     * @param loginInfo Login credentials
     * @throws Exception if login fails
     */
    public void login(LoginRequest loginInfo) throws Exception {
        HttpURLConnection http = createHttpConnection("/session", "POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        sendJsonBody(http, loginInfo);
        http.connect();

        int status = http.getResponseCode();
        if (status >= 200 && status < 300) {
            getAuthToken(http);
        } else {
            handleLoginError(status);
        }
    }

    /**
     * Extracts authentication token from server response.
     *
     * @param http HTTP connection with server response
     * @throws IOException if token extraction fails
     */
    private void getAuthToken(HttpURLConnection http) throws IOException {
        try (InputStream in = http.getInputStream()) {
            AuthData auth = gson.fromJson(new InputStreamReader(in), AuthData.class);
            this.authToken = auth.getAuthToken();
        }
    }

    /**
     * Logs out the current user.
     *
     * @throws Exception if logout fails
     */
    public void logout() throws Exception {
        validateAuthToken();
        HttpURLConnection http = createHttpConnection("/session", "DELETE");
        http.setRequestProperty("Authorization", authToken);
        http.connect();

        int status = http.getResponseCode();
        if (status >= 200 && status < 300) {
            this.authToken = null;
        } else {
            handleLogoutError(status);
        }
    }

    /**
     * Retrieves a list of available games.
     *
     * @return List of game data
     * @throws Exception if game listing fails
     */
    public List<GameData> listGames() throws Exception {
        validateAuthToken();
        HttpURLConnection http = createHttpConnection("/game", "GET");
        http.setRequestProperty("Authorization", authToken);
        http.connect();

        int status = http.getResponseCode();
        if (status >= 200 && status < 300) {
            return parseGameList(http);
        } else {
            handleGameListError(status);
            return null;
        }
    }

    /**
     * Creates a new game.
     *
     * @param newGame Game creation request details
     * @throws Exception if game creation fails
     */
    public void createGame(CreateGameRequest newGame) throws Exception {
        validateAuthToken();
        HttpURLConnection http = createHttpConnection("/game", "POST");
        http.setRequestProperty("Authorization", authToken);
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        sendJsonBody(http, newGame);
        http.connect();

        int status = http.getResponseCode();
        if (status < 200 || status > 300) {
            handleGameCreateError(status);
        }
    }

    /**
     * Joins an existing game.
     *
     * @param joinRequest Game join request details
     * @throws Exception if game joining fails
     */
    public void joinGame(GameJoinRequest joinRequest) throws Exception {
        validateAuthToken();
        HttpURLConnection http = createHttpConnection("/game", "PUT");
        http.setRequestProperty("Authorization", authToken);
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        sendJsonBody(http, joinRequest);
        http.connect();

        int status = http.getResponseCode();
        if (status < 200 || status > 300) {
            handleGameJoinError(status);
        }
    }

    // Helper methods for creating HTTP connections and handling errors
    private HttpURLConnection createHttpConnection(String endpoint, String method) throws Exception {
        URI uri = new URI(this.serverUrl + ":" + this.port + endpoint);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        return http;
    }

    private void sendJsonBody(HttpURLConnection http, Object data) throws IOException {
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = gson.toJson(data);
            outputStream.write(jsonBody.getBytes());
        }
    }

    private void validateAuthToken() throws Exception {
        if (this.authToken == null) {
            throw new Exception("Not logged in");
        }
    }

    // Error handling methods
    private void handleRegistrationError(int status) throws Exception {
        switch (status) {
            case 400: throw new Exception("Bad Request");
            case 403: throw new Exception("Username already taken");
            case 500: throw new Exception("Internal Server Error");
            default: throw new Exception("Error registering user");
        }
    }

    private void handleLoginError(int status) throws Exception {
        switch (status) {
            case 500: throw new Exception("Internal Server Error");
            case 401: throw new Exception("Unauthorized");
            default: throw new Exception("Error logging in");
        }
    }

    private void handleLogoutError(int status) throws Exception {
        switch (status) {
            case 401: throw new Exception("Unauthorized");
            case 500: throw new Exception("Internal Server Error");
            default: throw new Exception("Error logging out");
        }
    }

    private List<GameData> parseGameList(HttpURLConnection http) throws IOException {
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            Map<String, Object> responseMap = gson.fromJson(inputStreamReader, Map.class);
            List<Map<String, Object>> gameDataList = (List<Map<String, Object>>) responseMap.get("games");

            List<GameData> games = new ArrayList<>();
            for (Map<String, Object> gameData : gameDataList) {
                int gameID = ((Double) gameData.get("gameID")).intValue();
                String gameName = (String) gameData.get("gameName");
                String whiteUsername = (String) gameData.get("whiteUsername");
                String blackUsername = (String) gameData.get("blackUsername");
                games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, null));
            }

            return games;
        }
    }

    private void handleGameListError(int status) throws Exception {
        switch (status) {
            case 401: throw new Exception("Unauthorized");
            case 500: throw new Exception("Internal Server Error");
            default: throw new Exception("Error retrieving game list");
        }
    }

    private void handleGameCreateError(int status) throws Exception {
        switch (status) {
            case 400: throw new Exception("Bad Request");
            case 401: throw new Exception("Unauthorized");
            case 500: throw new Exception("Internal Server Error");
            default: throw new Exception("Error creating game");
        }
    }

    private void handleGameJoinError(int status) throws Exception {
        switch (status) {
            case 400: throw new Exception("Bad Request");
            case 401: throw new Exception("Unauthorized");
            case 403: throw new Exception("Color already taken");
            case 500: throw new Exception("Internal Server Error");
            default: throw new Exception("Error joining game");
        }
    }
}