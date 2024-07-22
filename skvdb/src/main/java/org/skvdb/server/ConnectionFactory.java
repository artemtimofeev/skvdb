package org.skvdb.server;

import org.skvdb.exception.NetworkException;
import org.skvdb.security.AuthenticationService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionFactory {
    private AuthenticationService authenticationService;

    private ServerSocket serverSocket;

    private QueryHandler queryHandler;

    public ConnectionFactory(int port) {
        this.authenticationService = new AuthenticationService();
        this.queryHandler = new QueryHandler();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    public Connection createConnection() {
        try {
            Socket clientSocket = serverSocket.accept();
            return new Connection(authenticationService, clientSocket, queryHandler);
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    public void close() {
        queryHandler.close();
    }
}
