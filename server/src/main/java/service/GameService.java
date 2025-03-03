package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.*;

import java.util.*;

public class GameService {
    private final AuthDAO authDAO = new AuthDAO(); // Data Access Object for authentication
    private final GameDAO gameDAO = new GameDAO(); // Data Access Object for game management

    /**
     * Creates a new game.
     *
     * @param authToken The authentication token of the user.
     * @param gameName  The name of the game to be created.
     * @return A response object, either a CreateGameResponse or an ErrorResponse.
     */
    public Object create(String authToken, String gameName) {
        // Validate the authentication token
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            return new ErrorResponse("Error: unauthorized", 401);
        }

        GameData game = gameDAO.createGame(gameName);

        // Return a response with the created game's ID
        return new CreateGameResponse(game.getGameID());
    }

    /**
     * Lists all available games.
     *
     * @param authToken The authentication token of the user.
     * @return A response object containing a list of games or an ErrorResponse.
     */
    public Object list(String authToken) {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            return new ErrorResponse("Error: unauthorized", 401); // Return error if unauthorized
        }

        List<GameData> gameList = gameDAO.getAllGames();

        List<GameDataDTO> gameDTOList = new ArrayList<>();
        for (GameData gameData : gameList) {
            gameDTOList.add(new GameDataDTO(gameData));
        }

        Map<String, List<GameDataDTO>> response = new HashMap<>();
        response.put("games", gameDTOList);

        return response; // Return the list of games
    }

    /**
     * Allows a user to join an existing game.
     *
     * @param authToken       The authentication token of the user.
     * @param gameJoinRequest The request containing the game ID and player color.
     * @return A response object, either the updated GameData or an ErrorResponse.
     */
    public Object join(String authToken, GameJoinRequest gameJoinRequest) {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            return new ErrorResponse("Error: unauthorized", 401); // Return error if unauthorized
        }
        GameData game = gameDAO.getGame(gameJoinRequest.getGameID());

        if (game == null) {
            return new ErrorResponse("Error: no game exists", 400); // Return error if no such game exists
        }

        // Handle joining as a WHITE player
        if (Objects.equals(gameJoinRequest.getPlayerColor(), "WHITE")) {
            if (game.getWhiteUsername() != null) {
                return new ErrorResponse("Error: white username taken", 403); // Return error if WHITE is already taken
            }

            // Assign the user as the WHITE player and update the game in DAO
            game.setWhiteUsername(authData.getUsername());
            gameDAO.updateGame(game);

            return game; // Return updated game data
        } else {
            // Handle joining as a BLACK player
            if (game.getBlackUsername() != null) {
                return new ErrorResponse("Error: black username taken", 403);
            }

            // Assign the user as the BLACK player and update the game in DAO
            game.setBlackUsername(authData.getUsername());
            gameDAO.updateGame(game);

            return game; // Return updated game data
        }
    }
}
