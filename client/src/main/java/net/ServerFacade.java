package net;
import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.net.*;
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
    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        this.username = loginRequest.username();
        LoginResult loginResult = this.makeRequest("POST", path, loginRequest, LoginResult.class);
        this.authToken = loginResult.authToken();
        return loginResult;
    }


}