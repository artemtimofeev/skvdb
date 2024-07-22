package org.skvdb.network;

import org.skvdb.dto.CloseConnectionDto;
import org.skvdb.dto.QueryDto;
import org.skvdb.dto.QueryResultDto;
import org.skvdb.dto.RequestResult;
import org.skvdb.exception.QueryException;
import org.skvdb.security.AuthenticationService;

public class Connection {
    private String token;
    private AuthenticationService authenticationService;
    private NetworkService networkService;

    Connection(String host, int port, String username, String password) {
        networkService = new NetworkService(host, port);
        authenticationService = new AuthenticationService(networkService);
        token = authenticationService.authenticate(username, password);
    }

    public Executor createExecutor() {
        return new Executor();
    }

    public void close() {
        networkService.send(new CloseConnectionDto("true", token));
        networkService.close();
    }

    public class Executor {
        public String get(String tableName, String key) {
            QueryResultDto queryResultDto = (QueryResultDto) networkService.send(new QueryDto("get", tableName, key, null, token));
            if (queryResultDto.result().equals(RequestResult.OK)) {
                return queryResultDto.value();
            }
            throw new QueryException("Ошибка во время выполнения запроса");
        }

        public void set(String tableName, String key, String value) {
            QueryResultDto queryResultDto = (QueryResultDto) networkService.send(new QueryDto("set", tableName, key, value, token));
            if (queryResultDto.result().equals(RequestResult.OK)) {
                return;
            }
            throw new QueryException("Ошибка во время выполнения запроса");
        }
    }
}
