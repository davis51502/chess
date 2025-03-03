package handlers;
import model.ResponseObject;
import service.DatabaseService;
import spark.*;
import com.google.gson.Gson;

public class DatabaseHandler {
    private final Gson gson = new Gson();
    private final DatabaseService databaseService = new DatabaseService();
    public Object clear(Request req, Response res) {
        res.status(200);
        res.type("application/json");
        int codeError = databaseService.clearAll();
        ResponseObject responseObject;
        if (codeError != 1) {
            responseObject= new ResponseObject("error code: " + codeError);
        }else {responseObject= new ResponseObject(null); }
        return gson.toJson(responseObject);
    }
}