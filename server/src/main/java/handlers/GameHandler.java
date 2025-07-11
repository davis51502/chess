package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class GameHandler {
    private GameService gameService;
    private Gson gson;
    public GameHandler(GameService gameService) {
        this.gameService = gameService;
        this.gson = new Gson();
    }
    public Object listGames(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            if (authToken == null || authToken.isEmpty()) {
                res.status(401);
                return gson.toJson(Map.of("message", "error: unauthorized"));
            }
            var juegos = gameService.listGames(authToken);
            res.status(200);
            return gson.toJson(Map.of("games", juegos));

        } catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized") || e.getMessage().contains("invalid") || e.getMessage().contains("user not found")) {
                res.status(401);
                return gson.toJson(Map.of("message", "error: unauthorized"));
            } else {
                res.status(500);
                return gson.toJson(Map.of("message", "error: " + e.getMessage()));
            }
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(Map.of("message", "error: " + e.getMessage()));
        }
        }
        public Object createGame(Request req, Response res) {
            try {
                String authToken = req.headers("Authorization");
                if (authToken == null || authToken.isEmpty()) {
                    res.status(401);
                    return gson.toJson(Map.of("message", "error: unauthorized"));
            }
                Map reqData = gson.fromJson(req.body(), Map.class);
                String juego = (String) reqData.get("gameName") ;
                if (juego == null || juego.isEmpty()) {
                    res.status(400);
                    return gson.toJson(Map.of("message", "error: bad request"));
                }
                int juegoBonito = gameService.createGame(authToken, juego);
                res.status(200);
                return gson.toJson(Map.of("gameID", juegoBonito));
        } catch (DataAccessException e) {
                if (e.getMessage().contains("unauthorized") || e.getMessage().contains("invalid") || e.getMessage().contains("user not found")) {
                    res.status(401);
                    return gson.toJson(Map.of("message", "error: unauthorized"));
                } else {
                    res.status(500);
                    return gson.toJson(Map.of("message", "error: " + e.getMessage()));
                }
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(Map.of("message", "error: " + e.getMessage()));
            }
            }
        public Object joinGame(Request req, Response res) {
        try{
        String authToken = req.headers("Authorization");
            if (authToken == null || authToken.isEmpty()) {
                res.status(401);
                return gson.toJson(Map.of("message", "error: unauthorized"));
            }
            Map reqData = gson.fromJson(req.body(), Map.class);
            if (reqData == null || !reqData.containsKey("gameID")) {
                res.status(400);
                return gson.toJson(Map.of("message", "error: bad request"));
            }
            String playerColor = (String) reqData.get("playerColor") ;
            Double gameDouble = (Double) reqData.get("gameID");

            if (gameDouble == null) {
                res.status(400);
                return gson.toJson(Map.of("message", "error: bad request"));
            }
            Integer gameID = gameDouble.intValue();
            // playerColor is allowed to be null for observers
            gameService.joinGame(authToken, playerColor, gameID);
            res.status(200);
            return "{}";

        } catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized") || e.getMessage().contains("invalid game name")|| e.getMessage().contains("user not found")) {
                res.status(401);
                return gson.toJson(Map.of("message", "error: unauthorized"));
            } else if (e.getMessage().contains("already taken") || e.getMessage().contains("taken")){
                res.status(403);
                return gson.toJson(Map.of("message", "error: already taken"));
            } else if (e.getMessage().contains("error: bad request")){
                res.status(400);
                return gson.toJson(Map.of("message", "error: bad request"));
            }else {
                res.status(500);
                return gson.toJson(Map.of("message", "error: " + e.getMessage()));
            }
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(Map.of("message", "error: " + e.getMessage()));
        }
    }
}

