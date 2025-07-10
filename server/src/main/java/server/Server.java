package server;

import handlers.ClearHandler;
import handlers.GameHandler;
import handlers.UserHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        ClearHandler clearHandler = new ClearHandler();
        UserHandler userHandler = new UserHandler();
        GameHandler gameHandler = new GameHandler();



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
