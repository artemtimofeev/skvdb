package org.skvdb.server.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientSocketService {
    private List<Socket> clientSocketList = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger();

    public void register(Socket socket) {
        clientSocketList.add(socket);
    }

    public void close() {
        for (Socket socket : clientSocketList) {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }
}
