package server;

import chess.ChessGame;
import com.google.gson.GsonBuilder;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.ChessGameDeserializer;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WSServer {
    private final AuthDAO authDAO = new AuthDAO();
    private final GameDAO gameDAO = new GameDAO();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ChessGame.class, new ChessGameDeserializer()).create();
    private static final Map<Integer, Set<Session>> gameSessions = new ConcurrentHashMap<>();
    private static final Map<Session, Integer> sessionToGameMap = new ConcurrentHashMap<>();
    public WSServer() {

    }
}
