package org.skvdb.network;

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
    public SocketAcceptor(ServerSettings serverSettings) throws IOException {
        this.serverSocket = new ServerSocket(serverSettings.port());
    }

    public Socket accept() throws IOException {
        return serverSocket.accept();
    }

    public void close() throws IOException {
        serverSocket.close();
    }
}
