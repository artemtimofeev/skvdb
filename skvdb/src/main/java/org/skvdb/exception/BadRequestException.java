package org.skvdb.exception;

public class BadRequestException extends Exception {
    public BadRequestException(Exception e) {
        super(e.getMessage());
    }
}
