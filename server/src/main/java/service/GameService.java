package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

import java.util.Collection;
import java.util.Collections;

//  list result. create, join
public class GameService {
    private DataAccess dataAccess;
    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }
    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        if (dataAccess.getAuth(authToken) == null) {
            throw new DataAccessException("error: unauthorized");
        }
        Collection<GameData> games = dataAccess.listGames();
        if (games ==null) {return Collections.emptyList();}
        return games;
    }
    public int createGame(String authToken, String name) throws DataAccessException {
        if (dataAccess.getAuth(authToken) == null) {
            throw new DataAccessException("error: unauthorized");
        }
        if (name == null|| name.isBlank()){
            throw new DataAccessException("error: invalid game name");
        }
        return dataAccess.createGame(name);
    }
    public void joinGame(String authToken, String playerColor, int gameID) throws DataAccessException {
        // check if user is authorized
        AuthData auth = dataAccess.getAuth(authToken);
        if (auth == null) {
            throw new DataAccessException("error: unauthorized");
        }
        // check if the game exists
        GameData game = dataAccess.getGame(gameID);
        if (game == null) {
            throw new DataAccessException("error: bad request");
        }
        // check which side the player wants to join: black or white
        if (playerColor != null && playerColor.equalsIgnoreCase("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new DataAccessException("error: already taken");
            }
            GameData gameUpdate = new GameData(
                    gameID,
                    auth.username(),
                    game.blackUsername(),
                    game.gameName());
                    dataAccess.updateGame(gameUpdate);
        } else if (playerColor != null && playerColor.equalsIgnoreCase("BLACK")) {
            if (game.blackUsername() != null) {
                throw new DataAccessException("error: already taken");
            }
            GameData gameUpdate = new GameData(gameID,
                    game.whiteUsername(),
                    auth.username(),
                    game.gameName());
                    dataAccess.updateGame(gameUpdate);

    } else {throw new DataAccessException("error: bad request");}

}
}
