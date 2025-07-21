package client;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import javax.swing.text.html.HTML;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.Map;

// handles http connections
public class ServerFacade {
    private final String serverURL;
    private final Gson gson = new Gson();
    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }
    public AuthData register(String username, String password, String email) throws Exception {
        return makeReq("POST", "/user",
                new UserData(username, password, email), AuthData.class, null);
    }
    public AuthData login(String username, String password) throws Exception {
        return makeReq("POST", "/session", new UserData(username, password, null), AuthData.class, null);
    }
    public void logout(String authToken) throws Exception {
        makeReq("DELETE", "/session", null, null, authToken);
    }
    public GameData createGame(String authToken, String gameName) throws Exception {
        var request = Map.of("gameName", gameName);
        return makeReq("POST", "/game", request, GameData.class, authToken);
    }
    public Collection<GameData> listGames() {

    }
    public void joinGame() {

    }
    private <T> T makeReq(String method, String path,
                                              Object req, Class<T> responseClass, String authToken) throws Exception {

    }
    private void writeReqBody (Object req , HttpURLConnection connection) throws IOException {

    }
    private <T> T readRespBody(HttpURLConnection connection,Class<T> respClass) throws IOException {

    }
    private void handleErrorResponse ( HttpURLConnection connection) throws IOException, Exception {

    }
    // to check if code is 2xx range
    private boolean isSuccessful(int status) {
        return status >= 200 && status < 300;
    }
    private record JoinGameReq(ChessGame.TeamColor playerColor, int gameID) {}
}
