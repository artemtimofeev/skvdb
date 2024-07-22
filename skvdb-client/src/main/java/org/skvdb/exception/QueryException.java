package org.skvdb.exception;

public class QueryException extends RuntimeException {
    public QueryException() {
        super();
    }

    public QueryException(String message) {
        super(message);
    }

    public QueryException(Exception e) {
        super(e.getMessage());
    }
}
