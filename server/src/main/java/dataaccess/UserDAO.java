package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public UserDAO(){
        // Load the Database
        try {
            DatabaseManager.startUp();
        } catch (DataAccessException e){
            e.printStackTrace();
        }
    }

    // Clears users from database
    public int clear() {
        try {
            try (var conn  = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement("DELETE FROM users")) {
                    preparedStatement.executeUpdate();
                }
            }
            return 1; // Success
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    // Adds a user
    public UserData createUser(UserData user) throws DataAccessException {
        // Check if user w same address exists
        if (getUser(user.getUsername()) != null) {
            throw new DataAccessException("Username is already taken.");
        }
        try (var conn = DatabaseManager.getConnection()) {
            String query = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getEmail());
                String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                preparedStatement.setString(3, hashedPassword);
                preparedStatement.executeUpdate();


            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error creating user.");
        }
    }

    // Returns user by username
    public UserData getUser(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            String query = "SELECT id, username, email, password_hash FROM users WHERE username = ?";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(
                                rs.getString("username"), rs.getString("passowrd_hash"), rs.getString("email")
                        );
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
}