package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Map;


// register, login logout
public class UserHandler {
    private UserService userService;
    private Gson gson;
    public UserHandler(UserService userService) {
        this.userService = userService;
        this.gson = new Gson();
    }
    public Object register(Request req, Response res) {

        try {
            UserData user = gson.fromJson(req.body(), UserData.class);
            if (user.username() == null || user.password() == null || user.email() == null ||
                    user.username().isEmpty() || user.password().isEmpty() || user.email().isEmpty()) {
                res.status(400);
                return gson.toJson(Map.of("message", "error: bad request"));
            }
            AuthData auth = userService.register(user);
            res.status(200);
            return gson.toJson(auth);


        } catch (DataAccessException e) {
            if (e.getMessage().contains("already taken") || e.getMessage().contains("already exists") || e.getMessage().contains("user not found")) {
                res.status(403);
                return gson.toJson(Map.of("message", "Error: already taken"));
            } else {
                res.status(500);
                return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
            }
            //handles any other random exceptions besides specifically DataAccessException
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
        }
    }
    public Object login(Request req, Response res) {
        try {
            UserData user = gson.fromJson(req.body(), UserData.class);
            if (user == null || user.username() == null || user.password() == null ||
                    user.username().isEmpty() || user.password().isEmpty()) {
                res.status(400);
                return gson.toJson(Map.of("message", "error: bad request"));
        }
            AuthData auth = userService.login(user.username(), user.password());
            res.status(200);
            return gson.toJson(auth);

    } catch (DataAccessException e) {
            return errorHandler(res, e);
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
        }
        }

        public Object logout(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            if (authToken == null || authToken.isEmpty()) {
                res.status(401);
                return gson.toJson(Map.of("message", "error: unauthorized"));
            }
            userService.logout(authToken);
            res.status(200);
            return "{}"; // empty json for valid logout
        } catch (DataAccessException e) {
            return errorHandler(res, e);
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
        }
        }

    public String errorHandler(Response res, DataAccessException e) {
        String message = e.getMessage().toLowerCase();
        if (message.contains("unauthorized") || message.contains("invalid") ||
                message.contains("user not found") || message.contains("incorrect pw") || message.contains("missing user or pw") ) {
            res.status(401);
            return gson.toJson(Map.of("message", "error: unauthorized"));
        } else if (message.contains("already taken")|| message.contains("already exists")) {
            res.status(403);
            return gson.toJson(Map.of("message", "Error: already taken"));
        } else if (message.contains("error: bad request") || message.contains("invalid user data") || message.contains("missing")){
            res.status(400);
            return gson.toJson(Map.of("message", "Error: bad request"));
        } else {
            res.status(500);
            return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}
