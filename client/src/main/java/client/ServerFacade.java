package client;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import model.AuthData;
import model.GameData;
import model.UserData;

import javax.swing.text.html.HTML;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
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
    public Collection<GameData> listGames(String authToken) throws Exception {
        record ListGamesResp(Collection<GameData> games) {}
        var response = makeReq("GET", "/game", null,ListGamesResp.class, authToken);
        return response.games;
    }
    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws Exception{
        Map<String, Object> req = new HashMap<>();
        req.put("gameID", gameID);
        if (playerColor != null){req.put("playerColor", playerColor);}
        makeReq("PUT", "/game", req, null, authToken) ;
    }
    protected HttpURLConnection openConnection(String path) throws Exception {
        URL url = new URL(serverURL +path);
        return (HttpURLConnection) url.openConnection();
    }
    private <T> T makeReq(String method, String path,
                                              Object req, Class<T> responseClass, String authToken) throws Exception {
        HttpURLConnection connection = null ;
        try {
            connection = openConnection(path);
            connection.setRequestMethod(method);
            connection.setReadTimeout(4567);
            if (authToken != null) {
                connection.addRequestProperty("Authorization" , authToken);
            }
            if (req != null) {
                writeReqBody(req, connection);
            }
            connection.connect();
            //check resp code
            if (!isSuccessful(connection.getResponseCode())){
                handleErrorResponse(connection);
            }
            return readRespBody(connection,responseClass);

        }finally {
            if (connection != null) {connection.disconnect();
            }
        }
    }
    private void writeReqBody (Object req , HttpURLConnection connection) throws IOException {
        connection.setDoOutput(true);
        connection.addRequestProperty("Content-type","application/json");
        try (OutputStream reqBody = connection.getOutputStream()) {
            String json = gson.toJson(req);
            reqBody.write(json.getBytes(StandardCharsets.UTF_8));
        }
    }
    private <T> T readRespBody(HttpURLConnection connection,Class<T> respClass) throws IOException {
        //return null if no resp body is expected/present
        if (respClass == null || connection.getContentLength() == 0) {return  null;}
        try (InputStream respBody = connection.getInputStream()) {
            InputStreamReader reader = new InputStreamReader(respBody);
            return gson.fromJson(reader, respClass);
        }
    }
    // basically the check engine light function
    private void handleErrorResponse ( HttpURLConnection connection) throws IOException, Exception {
        String errorMesg = String.format("Req failed with HTTP %d: %s",
                connection.getResponseCode(), connection.getResponseMessage());
        try (InputStream errorStream =connection.getErrorStream()) {
            if (errorStream != null) {
                String errorBody = new String(errorStream.readAllBytes());
                try {
                    Map<?,?> errormap = gson.fromJson(errorBody, Map.class);
                    if (errormap.containsKey("message")) {errorMesg += "\ndetails:" + errormap.get("message"); }
                } catch (Exception e) {
                    errorMesg += "\n" + errorBody;
                }
            }
        }
        throw new Exception(errorMesg);
    }
    // to check if code is 2xx range
    private boolean isSuccessful(int status) {
        return status >= 200 && status < 300;
    }

    public void clear() throws Exception {
    makeReq("DELETE", "/db", null, null, null);
    }

    private record JoinGameReq(ChessGame.TeamColor playerColor, int gameID) {}
}
