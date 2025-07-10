package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.handler.ErrorHandler;
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
                return gson.toJson(Map.of("message", "Error: bad request"));
            }
            AuthData auth = userService.register(user);
            res.status(200);
            return gson.toJson(auth);


        } catch (DataAccessException e) {
            if (e.getMessage().contains("already taken") || e.getMessage().contains("already exists")) {
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
                return gson.toJson(Map.of("message", "Error: bad request"));
        }
            AuthData auth = userService.login(user.username(), user.password());
            res.status(200);
            return gson.toJson(auth);

    } catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized") || e.getMessage().contains("invalid")) {
                res.status(401);
                return gson.toJson(Map.of("message", "Error: unauthorized"));
            } else {
                res.status(500);
                return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
            }
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
        }
        }
        public Object logout(Request req, Response res) {
        try {
            String authToken = req.headers("Auth");
            if (authToken == null || authToken.isEmpty()) {
                res.status(401);
                return gson.toJson(Map.of("message", "Error: unauthorized"));
            }
            userService.logout(authToken);
            res.status(200);
            return "{}"; // empty json for valid logout
        } catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized") || e.getMessage().contains("invalid")) {
                res.status(401);
                return gson.toJson(Map.of("message", "Error: unauthorized"));
            } else {
                res.status(500);
                return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
            }
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
        }
        }
    }
