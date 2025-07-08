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
}
