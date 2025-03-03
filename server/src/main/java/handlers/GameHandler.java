package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.ErrorResponse;
import model.GameJoinRequest;
import model.ResponseObject;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class GameHandler {

    private final Gson gson = new Gson();
    private final GameService gameService = new GameService();

    public Object create(Request req, Response res) {
        String authToken = req.headers("authorization");

        if (isInvalidAuthToken(authToken)) {
            res.status(401);
            return gson.toJson(new ResponseObject("Error: unauthorized"));
        }

        String gameName;
        try {
            JsonObject jsonBody = JsonParser.parseString(req.body()).getAsJsonObject();
            if (jsonBody.has("gameName")) {
                gameName = jsonBody.get("gameName").getAsString();
            } else {
                res.status(400);
                return gson.toJson(new ResponseObject("Error: bad request"));
            }
        } catch (Exception e) {
            res.status(400);
            return gson.toJson(new ResponseObject("Error: bad request"));
        }

        Object response = gameService.create(authToken, gameName);

        if (response instanceof ErrorResponse errorResponse) {
            if (errorResponse.getStatusCode() == 401) {
                res.status(401);
                return gson.toJson(new ResponseObject("Error: unauthorized"));
            }
        }

        res.status(200);
        return gson.toJson(response);
    }

    public Object list(Request req, Response res) {
        String authToken = req.headers("authorization");

        if (isInvalidAuthToken(authToken)) {
            res.status(401);
            return gson.toJson(new ResponseObject("Error: unauthorized"));
        }

        Object response = gameService.list(authToken);

        if (response instanceof ErrorResponse errorResponse) {
            if (errorResponse.getStatusCode() == 401) {
                res.status(401);
                return gson.toJson(new ResponseObject("Error: unauthorized"));
            }
        }

        res.status(200);
        return gson.toJson(response);
    }

    public Object join(Request req, Response res) {
        String authToken = req.headers("authorization");

        if (isInvalidAuthToken(authToken)) {
            res.status(401);
            return gson.toJson(new ResponseObject("Error: unauthorized"));
        }

        GameJoinRequest gameJoinRequest;
        try {
            gameJoinRequest = gson.fromJson(req.body(), GameJoinRequest.class);
        } catch (Exception e) {
            res.status(400);
            return gson.toJson(new ResponseObject("Error: bad request"));
        }

        if (!isValidPlayerColor(gameJoinRequest.getPlayerColor())) {
            res.status(400);
            return gson.toJson(new ResponseObject("Error: bad request"));
        }

        Object response = gameService.join(authToken, gameJoinRequest);

        if (response instanceof ErrorResponse errorResponse) {
            switch (errorResponse.getStatusCode()) {
                case 400 -> {
                    res.status(400);
                    return gson.toJson(new ResponseObject("Error: bad request"));
                }
                case 401 -> {
                    res.status(401);
                    return gson.toJson(new ResponseObject("Error: unauthorized"));
                }
                case 403 -> {
                    res.status(403);
                    return gson.toJson(new ResponseObject("Error: already taken"));
                }
                default -> throw new IllegalStateException("Unexpected status code");
            }
        }

        res.status(200);
        return gson.toJson(response);
    }

    private boolean isInvalidAuthToken(String authToken) {
        return authToken == null || authToken.isEmpty();
    }

    private boolean isValidPlayerColor(String playerColor) {
        return Objects.equals(playerColor, "WHITE") || Objects.equals(playerColor, "BLACK");
    }
}
