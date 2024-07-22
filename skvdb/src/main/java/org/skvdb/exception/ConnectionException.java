package org.skvdb.exception;

public class ConnectionException extends RuntimeException{
    public ConnectionException() {
        super();
    }

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(Exception e) {
        super(e.getMessage());
    }
}
