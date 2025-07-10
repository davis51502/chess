package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.ClearService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class ClearHandler {
    private ClearService clearService;
    private Gson gson;
    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
        this.gson = new Gson();
    }
    public Object clearApp(Request req, Response res){
        try{
            clearService.clearApp();
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}
