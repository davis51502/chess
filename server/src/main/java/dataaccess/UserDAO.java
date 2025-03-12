package dataaccess;

import model.UserData;

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
        for (UserData existingUser : userDataList) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                throw new DataAccessException("Username is already taken.");
            }
        }

        userDataList.add(user);

        return user;
    }

    // returns user by username
    public UserData getUser(String username) {
        for (UserData user : userDataList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}