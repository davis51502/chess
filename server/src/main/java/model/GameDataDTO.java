package model;

/**
 * Data Transfer Object (DTO) for game data.
 * This class is used to transfer game-related information between different layers of the application.
 */
public class GameDataDTO {

    // The unique identifier for the game
    private int gameID;

    // The username of the player playing as white
    private String whiteUsername;

    // The username of the player playing as black
    private String blackUsername;

    // The name of the game
    private String gameName;

    /**
     * Constructor to initialize a GameDataDTO object using a GameData object.
     *
     * @param gameData The GameData object containing the game's information.
     */
    public GameDataDTO(GameData gameData) {
        this.gameID = gameData.getGameID();
        this.whiteUsername = gameData.getWhiteUsername();
        this.blackUsername = gameData.getBlackUsername();
        this.gameName = gameData.getGameName();
    }

    /**
     * Sets the name of the game.
     *
     * @param gameName The name of the game.
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * Sets the unique identifier for the game.
     *
     * @param gameID The game's unique ID.
     */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    /**
     * Sets the username of the player playing as black.
     *
     * @param blackUsername The username of the black player.
     */
    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    /**
     * Sets the username of the player playing as white.
     *
     * @param whiteUsername The username of the white player.
     */
    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }
}
