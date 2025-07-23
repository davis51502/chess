package client;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

// holds session data like auth and game states
public class ClientState {
    private String authToken;
    private String username;
    private GameData currentGame;
    private List<GameData> gamesList = new ArrayList<>();
    private ChessGame.TeamColor playerColor;
    private boolean inGame = false;

    public boolean isLoggedIn() {
        return authToken != null;
    }

    public boolean isInGame() {
        return inGame;
    }

    public String getPrompted() {
        if (!isLoggedIn()) {
            return "[LOGGED_OUT] >>> ";
        }
        if (!isInGame()) {
            return "[LOGGED_IN] >>> ";
        }
        return "[IN_GAME] >>> ";
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public GameData getCurrGame() {
        return currentGame;
    }

    public void setCurrentGame(GameData currentGame) {
        this.currentGame = currentGame;
    }

    public ChessGame.TeamColor getWhichPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public List<GameData> getGamesList() {
        return gamesList;
    }

    public void setGamesList(List<GameData> gamesList) {
        this.gamesList = gamesList;
    }
}


