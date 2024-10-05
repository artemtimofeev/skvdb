package org.skvdb.exception;

public class TableNotFoundException extends Exception {
    public TableNotFoundException() {
        super();
    }

    public TableNotFoundException(String message) {
        super(message);
    }

    public TableNotFoundException(Exception e) {
        super(e.getMessage());
    }
}
