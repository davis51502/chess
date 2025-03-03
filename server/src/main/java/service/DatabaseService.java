package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class DatabaseService {
    // instances
    private final AuthDAO authDAO = new AuthDAO();
    private final GameDAO gameDAO = new GameDAO();
    private final UserDAO userDAO = new UserDAO();


    // clears database
    public int clearAll() {
        int sAuth = authDAO.clear();
        int sGame = gameDAO.clear();
        int sUser = userDAO.clear();

        if (sAuth != 1) {
            return sAuth;
        } else if (sGame != 1) {
            return sGame;
        } else return sUser;
    }
}
