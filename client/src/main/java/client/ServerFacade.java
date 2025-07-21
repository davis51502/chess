package client;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.util.Collection;

// handles http connections
public class ServerFacade {
    private final String serverURL;
    private final Gson gson = new Gson();
    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }
    public AuthData register() {

    }
    public AuthData login() {

    }
    public void logout() {

    }
    public GameData createGame() {

    }
    public Collection<GameData> listGames() {

    }
    public void joinGame() {

    }
    private <initialType> initialType makeReq(String method, String path,
                                              Object req, Class<initialType> responseClass, String authToken) throws Exception {

    }
    private record JoinGameReq(ChessGame.TeamColor playerColor, int gameID) {}
}
