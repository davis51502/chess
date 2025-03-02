package dataaccess;
import chess.ChessGame;
import model.GameData;
import model.UserData;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {
    private static List<GameData> gameDataList = new ArrayList<>();
    public int clear() {
        try {
            gameDataList.clear();
            return 1; // Success
        } catch (Exception e) {
            return -1; // Error
        }
    }

    public GameData createGame(String gameName) {
        GameData game = new GameData(
                gameDataList.size() + 1,
                null,
                null, gameName,
                new ChessGame());
        gameDataList.add(game);
        return game;
    }

    public GameData getGame(int gameId) {
        for (GameData game : gameDataList) {
            if (game.getGameID() == gameId) {
                return game;
            }
        }
        return null;
    }

    public List<GameData> getAllGames() {
        return new ArrayList<>(gameDataList);
    }

    public void updateGame(GameData game) {
        for (GameData existingGame : gameDataList) {
            if (existingGame.getGameID() == game.getGameID()) {
                existingGame.setGameName(game.getGameName());
                existingGame.setWhiteUsername(game.getWhiteUsername());
                existingGame.setBlackUsername(game.getBlackUsername());
            }
        }
    }
}
