package org.skvdb.server;

import org.skvdb.util.RandomStringGenerator;

import java.net.Socket;

public class Client {
    private Socket socket;
    private String id;

    public Socket getSocket() {
        return socket;
    }

    public String getId() {
        return id;
    }

    public Client(Socket socket) {
        id = new RandomStringGenerator().generateRandomString(15);
        this.socket = socket;
    }
}
