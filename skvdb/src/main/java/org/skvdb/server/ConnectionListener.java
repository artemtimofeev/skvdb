package org.skvdb.server;

import jakarta.annotation.PostConstruct;
import org.skvdb.controller.Controller;
import org.skvdb.network.SocketAcceptor;
import org.skvdb.security.AuthenticationService;
import org.skvdb.service.ServerStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

@Component
public class ConnectionListener {
    @Autowired
    private SocketAcceptor socketAcceptor;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private QueryHandler queryHandler;

    @Autowired
    private ServerStatusService serverStatusService;

    @Autowired
    private ConnectionPoolService connectionPoolService;

    @Autowired
    private ExecutorService connectionListenerExecutor;

    @Autowired
    private Controller controller;

    @PostConstruct
    public void listen() {
        connectionListenerExecutor.submit(() -> {
            while (serverStatusService.isRunning()) {
                if (connectionPoolService.hasFreeConnection()) {
                    Socket socket = null;
                    try {
                        socket = socketAcceptor.accept();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Connection connection = new Connection(authenticationService, socket, queryHandler, serverStatusService, controller);
                    connectionPoolService.startConnection(connection);
                }
            }
        });
    }
}
