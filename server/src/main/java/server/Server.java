package server;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.SQLDataAccess;
import handlers.ClearHandler;
import handlers.GameHandler;
import handlers.UserHandler;
import service.*;
import spark.*;

import java.sql.SQLException;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        DataAccess dataAccess;
        try {
            dataAccess = new SQLDataAccess();
        } catch (DataAccessException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            dataAccess = new MemoryDataAccess();
        }
//        DataAccess dataAccess = new MemoryDataAccess();

        UserService userService = new UserService(dataAccess);
        GameService gameService = new GameService(dataAccess);
        ClearService clearService = new ClearService(dataAccess);
        ClearHandler clearHandler = new ClearHandler(clearService);
        UserHandler userHandler = new UserHandler(userService);
        GameHandler gameHandler = new GameHandler(gameService);



        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::clearApp);
        // user
        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.delete("/session", userHandler::logout);
        // game
        Spark.get("/game",gameHandler::listGames );
        Spark.post("/game", gameHandler::createGame);
        Spark.put("/game", gameHandler::joinGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
