package org.skvdb.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.bf.ConnectionBeanFactory;
import org.skvdb.configuration.settings.ServerSettings;
import org.skvdb.exception.ClosedConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Service
public class ConnectionPool {
    private final ExecutorService service;

    private Semaphore freeConnections;

    private ConnectionBeanFactory connectionBeanFactory;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public ConnectionPool(ServerSettings settings, ConnectionBeanFactory connectionBeanFactory) {
        service = Executors.newFixedThreadPool(settings.connectionsPoolSize());
        freeConnections = new Semaphore(settings.connectionsPoolSize());
        this.connectionBeanFactory = connectionBeanFactory;
    }

    public void createAndStartConnection(Client client) {
        Connection connection = connectionBeanFactory.getConnection(client);
        startConnection(connection);
    }

    public void startConnection(Connection connection) {
        service.submit(() -> {
            try {
                freeConnections.acquire();
            } catch (InterruptedException e) {
                return;
            }
            try {
                connection.run();
            } catch (Exception e) {
                logger.info(e);
            } finally {
                connection.close();
            }
            freeConnections.release();
        });
    }

    public boolean hasFreeConnection() {
        return freeConnections.availablePermits() > 0;
    }

    public void shutdownNow() {
        service.shutdownNow();
    }
}
