package org.skvdb.server;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.server.network.SocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ConnectionListener {
    @Autowired
    private SocketAcceptor socketAcceptor;

    @Autowired
    private ConnectionPool connectionPool;

    private static final Logger logger = LogManager.getLogger(ConnectionListener.class);

    @PostConstruct
    public void listen() {
        ExecutorService connectionListenerExecutor = Executors.newSingleThreadExecutor();
        connectionListenerExecutor.submit(() -> {
            while (true) {
                if (connectionPool.hasFreeConnection()) {
                    Client client = new Client(socketAcceptor.accept());
                    logger.debug("Подключается клиент (id={}) с {}", client.getId(), client.getSocket().getRemoteSocketAddress());
                    connectionPool.createAndStartConnection(client);
                }
            }
        });

        connectionListenerExecutor.shutdown();
    }
}
