package dataaccess;
import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AuthDAO {
    private static List<AuthData> authDataList = new ArrayList<>();

    public int clear() {
        try {
            authDataList.clear();
            return 1; //success
        } catch (Exception e) {
            return -1; //error
        }
    }

    public AuthData createAuth(String username) {
        AuthData authdata = new AuthData(UUID.randomUUID().toString(), username);
        authDataList.add(authdata);
        return authdata;
    }

    public AuthData getAuth(String token) {
        for (AuthData auth : authDataList) {
            if (auth.getAuthToken().equals(token)) {
                return auth;
            }
        }
        return null;
    }
    public void deleteAuth(AuthData authData) {
        authDataList.remove(authData);
    }
}
