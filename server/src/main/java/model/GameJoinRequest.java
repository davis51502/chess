package model;

/**
 * Represents a request to join a game.
 * Contains the player's chosen color and the game ID they wish to join.
 */
public class GameJoinRequest {

    // The color chosen by the player (e.g., "WHITE" or "BLACK")
    private String playerColor;

    // The ID of the game the player wants to join
    private int gameID;

    /**
     * Constructor to initialize a GameJoinRequest with the specified player color and game ID.
     *
     * @param playerColor The color chosen by the player (e.g., "WHITE" or "BLACK").
     * @param gameID      The ID of the game the player wants to join.
     */
    public GameJoinRequest(String playerColor, int gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    /**
     * Gets the color chosen by the player.
     *
     * @return The player's chosen color (e.g., "WHITE" or "BLACK").
     */
    public String getPlayerColor() {
        return playerColor;
    }

    /**
     * Sets the color chosen by the player.
     *
     * @param playerColor The player's chosen color (e.g., "WHITE" or "BLACK").
     */
    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    /**
     * Gets the ID of the game the player wants to join.
     *
     * @return The game ID.
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * Sets the ID of the game the player wants to join.
     *
     * @param gameID The game ID.
     */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
