package org.skvdb.network;

public class ConnectionFactory {
    private String host;

    private int port;

    private String username;

    private String password;

    public Connection createConnection() {
        return new Connection(host, port, username, password);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
