package org.skvdb.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.Socket;

public class ClientTest {
    private static Socket socket;
    private static Client client1;
    private static Client client2;

    @BeforeAll
    public static void init() {
        socket = new Socket();
        client1 = new Client(socket);
        client2 = new Client(socket);
    }

    @Test
    public void getIdTest() {
        Assertions.assertNotEquals(client1.getId(), client2.getId());
    }

    @Test
    public void getSocketTest() {
        Assertions.assertEquals(client1.getSocket(), socket);
        Assertions.assertEquals(client2.getSocket(), socket);
    }
}
