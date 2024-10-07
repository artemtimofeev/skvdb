package org.skvdb.server.network;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.Socket;

public class ClientSocketServiceTest {
    private static ClientSocketService clientSocketService;
    private static Socket socket;

    @BeforeAll
    public static void registerSocket() {
        socket = new Socket();
        clientSocketService = new ClientSocketService();
        clientSocketService.register(socket);
    }

    @Test
    public void closeTest() {
        Assertions.assertDoesNotThrow(() -> clientSocketService.close());
        Assertions.assertTrue(socket.isClosed());
    }
}
