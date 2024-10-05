package org.skvdb.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class ServerStatusService {
    private ServerStatus serverStatus = ServerStatus.RUNNING;

    synchronized public ServerStatus getCurrentServerStatus() {
        return serverStatus;
    }

    synchronized public void shutdown() {
        serverStatus = ServerStatus.STOPPED;
    }

    synchronized public boolean isRunning() {
        return serverStatus.equals(ServerStatus.RUNNING);
    }
}
