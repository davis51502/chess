package dataaccess;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.*;


import static java.sql.Types.NULL;

public class SQLDataAccess implements DataAccess {
    public SQLDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
        try {
            var conn = DatabaseManager.getConnection();
            dataExtractor(conn);
        } catch (SQLException | DataAccessException ex){
            throw new DataAccessException(String.format("unable to configure database:%s", ex.getMessage()));
        }
    }

    private void dataExtractor(Connection conn) throws SQLException {
        try (conn) {
            for(var statement : createState) {
                try (var prepStatement = conn.prepareStatement(statement)) {
                    prepStatement.executeUpdate();
                }
            }
        }
    }
    public boolean verifyPw(String username, String normalPassword) throws DataAccessException {
        try {
            return verifyHelper(username, normalPassword);
        } catch (IllegalArgumentException e) {
            throw new DataAccessException("pw verification failed:" + e.getMessage());
        } catch (Exception e) {throw new DataAccessException("error verifying pw: " + e.getMessage()); }
    }

    private boolean verifyHelper(String username, String normalPassword) throws DataAccessException {
        UserData user = getUser(username);
        if (user == null) {
            return false;
        }
        return BCrypt.checkpw(normalPassword, user.password());
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var json  = rs.getString("json");
        return new Gson().fromJson(json, UserData.class);
    }

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
""", """
CREATE TABLE IF NOT EXISTS auth (
`token` varchar(256) NOT NULL,
`username` varchar(256) NOT NULL,
`json` TEXT DEFAULT NULL,
PRIMARY KEY (`token`),
INDEX(username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
"""

    };
    private int executeUpdate(String statement, Object...params) throws DataAccessException {
        try {
            var conn = DatabaseManager.getConnection();
            try (conn; var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                updateExactor(params, ps);
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                } return 0;
            }
        }
        catch (SQLException | DataAccessException ex) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, ex.getMessage()));
        }
    }

    private static void updateExactor(Object[] params, PreparedStatement ps) throws SQLException {
        for (var i = 0; i < params.length; i++) {
            var param = params[i];
            switch (param) {
                case String w -> ps.setString(i + 1, w);
                case Integer w -> ps.setInt(i + 1, w);
                case null -> ps.setNull(i + 1, NULL);
                default -> {
                }
            }
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if (getUser(userData.username()) != null) {
            throw new DataAccessException("username is already taken");
        }
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, userData.username());
                String hashPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
                ps.setString(2, hashPassword);
                ps.setString(3, userData.email());
                UserData hashuserdata = new UserData(userData.username(), hashPassword, userData.email());
                var json = new Gson().toJson(hashuserdata);
                ps.setString(4, json);
                ps.executeUpdate();
            }
        }catch (SQLException | DataAccessException e) {throw new DataAccessException("error creating user" + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {throw new DataAccessException(
                String.format("unable to read user data: %s", e.getMessage())); }
        return null;
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, username);
        var statement = "INSERT INTO auth (token, username, json) VALUES (?, ?, ?)";
        var json = new Gson().toJson(authData);
        executeUpdate(statement, token, username, json);
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT token, json FROM auth WHERE token=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(String.format("unable to read auth data : %s", e.getMessage())); }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE token=?";
        executeUpdate(statement, authToken);
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO game (game_name, json) VALUES (?, ?)";
        GameData gameData = new GameData(0, null, null, gameName);
        var json = new Gson().toJson(gameData);
        return executeUpdate(statement, gameName, json);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT game_ID, json FROM game WHERE game_ID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {return readGame(rs);}
                }
            }
        }catch (SQLException | DataAccessException e) {
            throw new DataAccessException(String.format("unable to read game data : %s", e.getMessage())); }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var statement = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var state  = "SELECT game_ID, json FROM game";
            try (var ps  = conn.prepareStatement(state)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        statement.add(readGame(rs));
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(String.format("unable to read games data: %s", e.getMessage()));
        } return statement;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        var statement = "UPDATE game SET white_username=?, black_username=?, game_name=?, json=? WHERE game_ID=?";
        var json = new Gson().toJson(game);
        executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), json, game.gameID());
    }

    @Override
    public void clear() throws DataAccessException {
        var statements = new String[] {
                "DELETE FROM auth",
                "DELETE FROM game",
                "DELETE FROM user",
        };
        for (var statement: statements) {
            executeUpdate(statement);
        }
    }
}
