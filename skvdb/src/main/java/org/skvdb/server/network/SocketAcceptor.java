package org.skvdb.server.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.configuration.settings.ServerSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class SocketAcceptor {
    private ServerSocket serverSocket;

    @Autowired
    private ClientSocketService clientSocketService;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public SocketAcceptor(ServerSettings serverSettings) throws IOException {
        this.serverSocket = new ServerSocket(serverSettings.port());
    }

    public Socket accept() {
        try {
            Socket socket = serverSocket.accept();
            clientSocketService.register(socket);
            return socket;
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
