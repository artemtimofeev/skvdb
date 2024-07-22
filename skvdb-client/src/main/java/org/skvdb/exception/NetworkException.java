package org.skvdb.exception;

public class NetworkException extends RuntimeException{
    public NetworkException() {
        super();
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(Exception e) {
        super(e.getMessage());
    }
}
