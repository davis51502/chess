package model;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username =username;
        this.password = password;
    }
    //get username, set username, get pw, set pw
    public String getUsername() {return username; }
    public void setUsername(String username) {this.username = username; }
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password; }
}