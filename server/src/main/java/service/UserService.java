package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

// register, login, logout
public class UserService {
    private DataAccess dataAccess;
    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }
    public AuthData register(UserData usersdata) throws DataAccessException {
        // verify input, check to make sure user isn't already taken
        if (dataAccess.getUser(usersdata.username()) != null) {
            throw new DataAccessException("error: username already exists");
        }
        if (usersdata.username() == null || usersdata.username().isBlank()) {
            throw new DataAccessException("error: invalid user data");
        }
        // insert new user into database
        dataAccess.createUser(usersdata);
        // login the user in the database and return
        return dataAccess.createAuth(usersdata.username());
    }
    public AuthData login(String username, String password) throws DataAccessException {
        UserData user = dataAccess.getUser(username);
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new DataAccessException("error: missing user or pw");
        }
        if (user == null) {
            throw new DataAccessException("error: user not found");
        }
        if (!user.password().equals(password)) {
            throw new DataAccessException("error: incorrect pw");
        }
        return dataAccess.createAuth(username);
    }

    public void logout(String authToken) throws DataAccessException {
        // always verify the input
        if (authToken == null || authToken.isBlank()) {
            throw new DataAccessException("error: missing authentication token");
        }
        if (dataAccess.getAuth(authToken) == null) {
            throw new DataAccessException("Error:not authorized");
        }
        dataAccess.deleteAuth(authToken);
    }
}
