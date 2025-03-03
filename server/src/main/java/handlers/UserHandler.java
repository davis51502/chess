package handlers;

import com.google.gson.Gson;
import model.ErrorResponse;
import model.LoginRequest;
import model.ResponseObject;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler {

    private final Gson gson = new Gson();
    private final UserService userService = new UserService();

    public Object register(Request req, Response res) {
        UserData userData;

        try {
            userData = gson.fromJson(req.body(), UserData.class);
        } catch (Exception e) {
            res.status(400);
            return gson.toJson(new ResponseObject("Error: bad request"));
        }

        if (isInvalidUserData(userData)) {
            res.status(400);
            return gson.toJson(new ResponseObject("Error: bad request"));
        }

        Object response = userService.register(userData);

        if (response == null) {
            res.status(500);
            return gson.toJson(new ResponseObject("Error: An unknown error occurred on the server"));
        }

        if (response instanceof ErrorResponse errorResponse && errorResponse.getStatusCode() == 403) {
            res.status(403);
            return gson.toJson(new ResponseObject("Error: already taken"));
        }

        res.status(200);
        return gson.toJson(response);
    }

    public Object login(Request req, Response res) {
        LoginRequest loginRequest;

        try {
            loginRequest = gson.fromJson(req.body(), LoginRequest.class);
        } catch (Exception e) {
            res.status(401);
            return gson.toJson(new ResponseObject("Error: unauthorized"));
        }

        if (isInvalidLoginRequest(loginRequest)) {
            res.status(401);
            return gson.toJson(new ResponseObject("Error: unauthorized"));
        }

        Object response = userService.login(loginRequest);

        if (response instanceof ErrorResponse errorResponse && errorResponse.getStatusCode() == 401) {
            res.status(401);
            return gson.toJson(new ResponseObject("Error: unauthorized"));
        }

        res.status(200);
        return gson.toJson(response);
    }

    public Object logout(Request req, Response res) {
        String authToken = req.headers("authorization");

        if (isInvalidAuthToken(authToken)) {
            res.status(401);
            return gson.toJson(new ResponseObject("Error: unauthorized"));
        }

        Object response = userService.logout(authToken);

        if (response instanceof ErrorResponse errorResponse && errorResponse.getStatusCode() == 401) {
            res.status(401);
            return gson.toJson(new ResponseObject("Error: unauthorized"));
        }

        res.status(200);
        return gson.toJson(new ResponseObject(null));
    }

    // Helper method to validate user data for registration
    private boolean isInvalidUserData(UserData userData) {
        return userData == null ||
                userData.getUsername() == null || userData.getUsername().isEmpty() ||
                userData.getPassword() == null || userData.getPassword().isEmpty() ||
                userData.getEmail() == null || userData.getEmail().isEmpty();
    }

    // Helper method to validate login request data
    private boolean isInvalidLoginRequest(LoginRequest loginRequest) {
        return loginRequest == null ||
                loginRequest.getUsername() == null || loginRequest.getUsername().isEmpty() ||
                loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty();
    }

    // Helper method to validate authorization token
    private boolean isInvalidAuthToken(String authToken) {
        return authToken == null || authToken.isEmpty();
    }
}
