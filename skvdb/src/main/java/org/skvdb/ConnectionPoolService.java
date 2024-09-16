package org.skvdb;

import org.skvdb.configuration.settings.ServerSettings;
import org.skvdb.server.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Service
public class ConnectionPoolService {
    private final ExecutorService service;

    private Semaphore freeConnections;

    @Autowired
    public ConnectionPoolService(ServerSettings settings) {
        service = Executors.newFixedThreadPool(settings.connectionsPoolSize());
        freeConnections = new Semaphore(settings.connectionsPoolSize());
    }

    public void startConnection(Connection connection) {
        service.submit(() -> {
            try {
                freeConnections.acquire();
                connection.run();
                freeConnections.release();
            } catch (InterruptedException e) {
                return;
            }
        });
    }

    public boolean hasFreeConnection() {
        return freeConnections.availablePermits() > 0;
    }

    public void shutdownNow() {
        service.shutdownNow();
    }
}
