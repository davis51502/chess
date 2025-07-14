package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.*;
import java.util.*;


import static java.sql.Types.NULL;

public class SQLDataAccess implements DataAccess {
    public SQLDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for(var statement : createState) {
                try (var prepStatement = conn.prepareStatement(statement)) {
                    prepStatement.executeUpdate();
                }
            }
        } catch (SQLException ex){
            throw new DataAccessException(String.format("unable to configure database:%s", ex.getMessage()));}
    }
    private UserData readUser(ResultSet rs) throws SQLException {var json  = rs.getString("json");
    return new Gson().fromJson(json, UserData.class);}

    private AuthData readAuth(ResultSet rs) throws SQLException {var json  = rs.getString("json");
        return new Gson().fromJson(json, AuthData.class);}

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("game_ID");
        var json  = rs.getString("json");
        var game = new Gson().fromJson(json, GameData.class);
        return new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName()); }

    private final String[] createState = {
    """
CREATE TABLE IF NOT EXISTS user (
`username` varchar(256) NOT NULL,
`password` varchar(256) NOT NULL,
`email` varchar(256) NOT NULL,
`json` TEXT DEFAULT NULL,
PRIMARY KEY (`username`),\s
INDEX(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci\s
""", """
CREATE TABLE IF NOT EXISTS auth (
`token` varchar(256) NOT NULL,
`username` varchar(256) NOT NULL,
`json` TEXT DEFAULT NULL,
PRIMARY KEY (`token`),
INDEX(username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci\s
""", """
CREATE TABLE IF NOT EXISTS game (
`game_ID` int NOT NULL AUTO_INCREMENT,
`white_username` varchar(256) DEFAULT NULL,
`black_username` varchar(256) DEFAULT NULL,
`game_name` varchar(256) DEFAULT NULL,
`json` TEXT DEFAULT NULL,
PRIMARY KEY (`game_ID`),
INDEX(white_username),
INDEX(black_username),
INDEX(game_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci\s
"""

    };
    private int executeUpdate(String statement, Object...params) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (var i= 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String w) ps.setString(i+1,w);
                    else if (param instanceof Integer w) ps.setInt(i+1,w);
                    else if (param == null) ps.setNull(i+1, NULL);
                }
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                } return 0;
            } catch (SQLException e) {
                throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
            }
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(userData);
        executeUpdate(statement, userData.username(), userData.password(), userData.email(), json);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return
                    }
                }
            }
        }
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
