package ui;
import model.*;
import net.ClientCommunicator;
import net.ServerFacade;
import java.util.Arrays;

public class PreLoginClient implements ClientObject {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private final ClientCommunicator notificationHandler;
    private String authToken;
    private boolean isPreLoginStage;
    private boolean isPostLoginStage;
    private boolean isInGameStage;

    public PreLoginClient(String serverUrl, ClientCommunicator notificationHandler, ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
        resetStages();
    }

    private void resetStages(){
        isPreLoginStage = false;
        isPostLoginStage = false;
        isInGameStage = false;
    }
}