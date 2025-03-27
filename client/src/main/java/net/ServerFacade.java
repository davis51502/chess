package net;
import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import chess.InvalidMoveException;

public class ServerFacade {
    private final String serverUrl;
    private String username;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
        this.username=null;
        this.authToken=null;
    }
    public String getAuth() {
        return authToken;
    }
    public RegisterResult register(RegisterRequest registerRequest) throws InvalidMoveException {
        var path = "/user";
        RegisterResult registerResult = this.makeRequest("POST", path, registerRequest, RegisterResult.class);
        this.username = registerResult.username();
        this.authToken = registerResult.authToken();
        return registerResult;
    }
    public LoginResult login(LoginRequest loginRequest) throws InvalidMoveException {
        var path = "/session";
        this.username = loginRequest.username();
        LoginResult loginResult = this.makeRequest("POST", path, loginRequest, LoginResult.class);
        this.authToken = loginResult.authToken();
        return loginResult;
    }
    public void logout() throws InvalidMoveException {
        LogoutRequest logoutRequest = new LogoutRequest(this.authToken);
        var path = "/session";
        this.makeRequest("DELETE", path, logoutRequest, null);
        this.authToken = null;
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws InvalidMoveException {
        var path = "/game";
        return this.makeRequest("GET", path,null, ListGamesResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws InvalidMoveException {
        var path = "/game";
        return this.makeRequest("POST", path, createGameRequest, CreateGameResult.class);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws InvalidMoveException {
        var path = "/game";
        this.makeRequest("PUT", path, joinGameRequest, null);
    }

    public void clear() throws InvalidMoveException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws InvalidMoveException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (InvalidMoveException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidMoveException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, InvalidMoveException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            String errorDetails = extractErrorDetails(http);
            throw new InvalidMoveException(status, errorDetails);
        }
    }
    private String extractErrorDetails(HttpURLConnection http) {
        try (InputStream errorStream = http.getErrorStream()) {
            if (errorStream == null) {
                return "No error details available. Status: " + http.getResponseCode();
            }

            String rawError = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
            return rawError.length() > 200
                    ? rawError.substring(0, 200) + "..."
                    : rawError;
        } catch (IOException e) {
            return "Could not read error stream: " + e.getMessage();
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}