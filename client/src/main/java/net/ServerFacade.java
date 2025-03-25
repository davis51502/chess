package net;

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
    public RegisterResult register(RegisterRequest registerRequest_)
}