package org.skvdb.network;

import org.skvdb.storage.StorageImpl;
import org.skvdb.storage.api.Storage;
import org.skvdb.dto.*;
import org.skvdb.exception.QueryException;
import org.skvdb.security.AuthenticationService;

import java.util.HashMap;
import java.util.Map;

public class Connection {
    private String username;
    private String password;
    private AuthenticationService authenticationService;
    private NetworkService networkService;

    Connection(String host, int port, String username, String password) {
        networkService = new NetworkService(host, port);
        authenticationService = new AuthenticationService(networkService);
        this.username = username;
        this.password = password;
    }

    public Executor createExecutor() {
        return new Executor();
    }

    public void close() {
        networkService.close();
    }

    public class Executor {
        public Storage getStorage() {
            return new StorageImpl(networkService, username, password);
        }

        public void createUser(String username1, String password1, boolean isSuperuser) {
            Map<String, String> body = new HashMap<>();
            body.put("username", username1);
            body.put("password", password1);
            body.put("isSuperuser", String.valueOf(isSuperuser));
            Request request = new Request(username, password, null, "create_user", body);
            Result result = networkService.send(request);
            if (result.getRequestResult().equals(RequestResult.OK)) {
                return;
            }
            throw new RuntimeException();
        }
    }
}
