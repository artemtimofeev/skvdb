package org.skvdb.exception;

import java.io.IOException;

public class ClosedConnectionException extends IOException {
    public ClosedConnectionException() {
        super();
    }

    public ClosedConnectionException(String message) {
        super(message);
    }

    public ClosedConnectionException(Exception e) {
        super(e.getMessage());
    }
}
