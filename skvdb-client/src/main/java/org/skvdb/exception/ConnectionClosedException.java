package org.skvdb.exception;

public class ConnectionClosedException extends RuntimeException {
    public ConnectionClosedException(String message) {
        super(message);
    }
}
