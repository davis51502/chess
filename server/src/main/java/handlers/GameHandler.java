package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
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
            String authToken = req.headers("Auth");
            if (authToken == null || authToken.isEmpty()) {
                res.status(401);
                return gson.toJson(Map.of("message", "Error: unauthorized"));
            }
            var juegos = gameService.listGames(authToken);
            res.status(200);
            return gson.toJson(Map.of("games", juegos));

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
        public Object createGame(Request req, Response res) {
            try {
                String authToken = req.headers("Auth");
                if (authToken == null || authToken.isEmpty()) {
                    res.status(401);
                    return gson.toJson(Map.of("message", "Error: unauthorized"));
            }
                Map reqData = gson.fromJson(req.body(), Map.class);
                String juego = (String) reqData.get("gameName") ;
                if (juego == null || juego.isEmpty()) {
                    res.status(400);
                    return gson.toJson(Map.of("message", "Error: bad request"));
                }
                int juegoBonito = gameService.createGame(authToken, juego);
                res.status(200);
                return gson.toJson(Map.of("gameID", juegoBonito));
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
        public Object joinGame(Request req, Response res) {
        try{
        String authToken = req.headers("Auth");
            if (authToken == null || authToken.isEmpty()) {
                res.status(401);
                return gson.toJson(Map.of("message", "Error: unauthorized"));
            }
            Map reqData = gson.fromJson(req.body(), Map.class);
            String juego = (String) reqData.get("gameName") ;
            Double gameDouble = (Double) reqData.get("gameID");
            Integer gameID = gameDouble.intValue();
            if (gameID == null) {
                res.status(400);
                return gson.toJson(Map.of("message", "Error: bad request"));
            }
            // playerColor is allowed to be null for observers
            gameService.joinGame(authToken, juego, gameID);
            res.status(200);
            return "{}";

        } catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized") || e.getMessage().contains("invalid")) {
                res.status(401);
                return gson.toJson(Map.of("message", "Error: unauthorized"));
            } else if (e.getMessage().contains("already taken") || e.getMessage().contains("taken")){
                res.status(403);
                return gson.toJson(Map.of("message", "Error: already taken"));
            } else if (e.getMessage().contains("bad req")){
                res.status(400);
                return gson.toJson(Map.of("message", "Error: bad req"));
            }else {
                res.status(500);
                return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
            }
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}

