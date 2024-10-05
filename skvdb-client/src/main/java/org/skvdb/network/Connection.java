package org.skvdb.network;

import org.skvdb.dto.*;
import org.skvdb.exception.QueryException;
import org.skvdb.security.AuthenticationService;

import java.util.HashMap;
import java.util.Map;

public class Connection {
    private String token;
    private String username;
    private String password;
    private AuthenticationService authenticationService;
    private NetworkService networkService;

    Connection(String host, int port, String username, String password) {
        networkService = new NetworkService(host, port);
        authenticationService = new AuthenticationService(networkService);
        this.username = username;
        this.password = password;
        //token = authenticationService.authenticate(username, password);
    }

    public Executor createExecutor() {
        return new Executor();
    }

    public void close() {
        networkService.close();
    }

    public class Executor {
        public String get(String tableName, String key) {
            Map<String, String> body = new HashMap<>();
            body.put("key", key);
            Result result = networkService.send(new Request(username, password, token, "get", body));
            if (result.getRequestResult().equals(RequestResult.OK)) {
                return result.getBody().get("value");
            }
            throw new QueryException("Ошибка во время выполнения запроса");
        }

        public void set(String tableName, String key, String value) {
            Map<String, String> body = new HashMap<>();
            body.put("key", key);
            body.put("value", value);
            body.put("table", tableName);
            Result result = networkService.send(new Request(username, password, "token", "set", body));
            if (result.getRequestResult().equals(RequestResult.OK)) {
                return;
            }
            throw new QueryException("Ошибка во время выполнения запроса");
        }
    }
}
